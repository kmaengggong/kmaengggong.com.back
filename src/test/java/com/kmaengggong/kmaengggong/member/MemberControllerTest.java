package com.kmaengggong.kmaengggong.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.kmaengggong.kmaengggong.member.interfaces.MemberRequest;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private MemberRequest memberRequest;

    private String email = "email@email.com";
    private String password = "password";
    private String nickname = "nickname";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        memberRequest = MemberRequest.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }

    @Test
    @Transactional
    @DisplayName("CR: saveAndFindById")
    void saveAndfindByIdTest() throws Exception {
        // Given
        String memberRequestJson = objectMapper.writeValueAsString(memberRequest);

        // When
        MvcResult result = mockMvc.perform(post("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRequestJson))
            .andExpect(status().isCreated())
            .andReturn();
        String uri = result.getResponse().getHeader("Location");

        // Then
        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.nickname").value(nickname));
    }

    // TODO: @DisplayName("C: saveAlreadyExits")

    @Test
    @Transactional
    @DisplayName("R: findAllPageTest")
    void findAllPageTest() throws Exception {
        // Given
        String email1 = "email1@email1.com";
        String password1 = "password1";
        String nickname1 = "nickname1";
        MemberRequest memberRequest1 = MemberRequest.builder()
            .email(email1)
            .password(password1)
            .nickname(nickname1)
            .build();

        String memberRequestJson = objectMapper.writeValueAsString(memberRequest);
        String memberRequestJson1 = objectMapper.writeValueAsString(memberRequest1);

        // When
        mockMvc.perform(post("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRequestJson))
            .andExpect(status().isCreated());
        mockMvc.perform(post("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRequestJson1))
            .andExpect(status().isCreated());

        String responseJson = mockMvc.perform(get("/member"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<String> expectedEmails = Arrays.asList(email, email1);
        List<String> jsonEmails = JsonPath.parse(responseJson).read("$..email");
        List<String> expectedNicknames = Arrays.asList(nickname, nickname1);
        List<String> jsonNicknames = JsonPath.parse(responseJson).read("$..nickname");

        // Then
        assertThat(jsonEmails).containsExactlyInAnyOrderElementsOf(expectedEmails);
        assertThat(jsonNicknames).containsExactlyInAnyOrderElementsOf(expectedNicknames);
    }

    @Test
    @DisplayName("R: findByIdNotExists")
    void findByIdNoContentTest() throws Exception {
        // Then
        mockMvc.perform(get("/member/1"))
            .andExpect(status().isNotFound());
    }

    // TODO: sorting
    // TODO: authorization

    @Test
    @Transactional
    @DisplayName("U: update")
    void updateTest() throws Exception {
        // Given
        String updateNickname = "updateNickname";
        String memberRequestJson = objectMapper.writeValueAsString(memberRequest);

        // When
        MvcResult result = mockMvc.perform(post("/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRequestJson))
            .andExpect(status().isCreated())
            .andReturn();
        String uri = result.getResponse().getHeader("Location");

        MemberRequest memberRequestUpdate = MemberRequest.builder()
            .nickname(updateNickname)
            .build();
        String memberRequestJsonUpdate = objectMapper.writeValueAsString(memberRequestUpdate);

        mockMvc.perform(post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRequestJsonUpdate))
            .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get(uri))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value(updateNickname));        
    }

    @Test
    @DisplayName("U: updatePassword")
    void updatePasswordTest() throws Exception {

    }

    @Test
    @DisplayName("U: updateTestNotExits")
    void updateTestNotExitsTest() throws Exception {

    }

    @Test
    @DisplayName("D: delete")
    void deleteTest() throws Exception {

    }

    @Test
    @DisplayName("D: deleteNotExists")
    void deleteNotExistsTest() throws Exception {

    }
}
