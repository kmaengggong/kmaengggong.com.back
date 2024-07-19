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
import com.kmaengggong.kmaengggong.member.interfaces.dto.MemberRequest;

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
	void saveAndFindByIdTest() throws Exception {
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

	@Test
	@Transactional
	@DisplayName("R: findAllPage")
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
		List<String> expectedNicknames = Arrays.asList(nickname, nickname1);
		List<String> jsonEmails = JsonPath.parse(responseJson).read("$..email");
		List<String> jsonNicknames = JsonPath.parse(responseJson).read("$..nickname");

		// Then
		assertThat(jsonEmails).containsExactlyInAnyOrderElementsOf(expectedEmails);
		assertThat(jsonNicknames).containsExactlyInAnyOrderElementsOf(expectedNicknames);
	}

	@Test
	@Transactional
	@DisplayName("R: findAllSortedPage")
	void findAllSortedPageTest() throws Exception {
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

		String responseJson = mockMvc.perform(get("/member?sort=registeredAt,desc"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<String> expectedEmails = Arrays.asList(email1, email);
		List<String> expectedNicknames = Arrays.asList(nickname1, nickname);
		List<String> jsonEmails = JsonPath.parse(responseJson).read("$..email");
		List<String> jsonNicknames = JsonPath.parse(responseJson).read("$..nickname");

		// Then
		assertThat(jsonEmails).containsExactlyInAnyOrderElementsOf(expectedEmails);
		assertThat(jsonNicknames).containsExactlyInAnyOrderElementsOf(expectedNicknames);
	}

	@Test
	@Transactional
	@DisplayName("R: findAllNotExistsPage")
	void findAllNotExistsPageTest() throws Exception {
		// Given
		String memberRequestJson = objectMapper.writeValueAsString(memberRequest);

		// When
		mockMvc.perform(post("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJson))
			.andExpect(status().isCreated());
		
		// Then
		String responseJson = mockMvc.perform(get("/member?page=9999&size=1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<String> expectedEmails = Arrays.asList(email);
		List<String> expectedNicknames = Arrays.asList(nickname);
		List<String> jsonEmails = JsonPath.parse(responseJson).read("$..email");
		List<String> jsonNicknames = JsonPath.parse(responseJson).read("$..nickname");

		// Then
		assertThat(jsonEmails).containsExactlyInAnyOrderElementsOf(expectedEmails);
		assertThat(jsonNicknames).containsExactlyInAnyOrderElementsOf(expectedNicknames);
	}

	@Test
	@DisplayName("R: findByIdNotExists")
	void findByIdNotExistsTest() throws Exception {
		// Given
		String uri = "/member/9999";

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isNotFound());
	}

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

		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJsonUpdate))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value(updateNickname));        
	}

	@Test
	@Transactional
	@DisplayName("U: updatePassword")
	void updatePasswordTest() throws Exception {
		// Given
		String updatePassword = "updatePassword";
		String memberRequestJson = objectMapper.writeValueAsString(memberRequest);

		// When
		MvcResult result = mockMvc.perform(post("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");
		String updatePasswordUri = uri + "/password";  // ex) /member/1 -> /member/1/password

		MemberRequest memberRequestUpdate = MemberRequest.builder()
			.password(updatePassword)
			.build();
		String memberRequestJsonUpdate = objectMapper.writeValueAsString(memberRequestUpdate);

		mockMvc.perform(patch(updatePasswordUri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJsonUpdate))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk());
	}

	@Test
	@Transactional
	@DisplayName("U: updateNotExits")
	void updateNotExitsTest() throws Exception {
		// Given
		String updateNickname = "updateNickname";
		String uri = "/member/9999";

		// When
		MemberRequest memberRequestUpdate = MemberRequest.builder()
			.nickname(updateNickname)
			.build();
		String memberRequestJsonUpdate = objectMapper.writeValueAsString(memberRequestUpdate);

		// Then
		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJsonUpdate))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("U: updateNullValue")
	void updateNullValue() throws Exception {
		// Given
		String updateNickname = null;
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

		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJsonUpdate))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value(nickname));   
	}

	@Test
	@Transactional
	@DisplayName("D: deleteById")
	void deleteByIdTest() throws Exception {
		// Given
		String memberRequestJson = objectMapper.writeValueAsString(memberRequest);

		// When
		MvcResult result = mockMvc.perform(post("/member")
			.contentType(MediaType.APPLICATION_JSON)
			.content(memberRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");

		mockMvc.perform(delete(uri))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("D: deleteByIdNotExists")
	void deleteByIdNotExistsTest() throws Exception {
		// Given
		String uri = "/member/9999";

		// Then
		mockMvc.perform(delete(uri))
			.andExpect(status().isNotFound());
	}
}
