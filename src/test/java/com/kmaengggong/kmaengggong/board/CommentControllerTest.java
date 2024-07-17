// package com.kmaengggong.kmaengggong.board;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.Arrays;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.jayway.jsonpath.JsonPath;
// import com.kmaengggong.kmaengggong.board.interfaces.dto.CommentRequest;
// import com.kmaengggong.kmaengggong.common.CommonTest;

// import jakarta.transaction.Transactional;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ExtendWith(MockitoExtension.class)
// public class CommentControllerTest extends CommonTest {
//     @Autowired
//     private MockMvc mockMvc;

//     private ObjectMapper objectMapper;
//     private CommentRequest commentRequest;

// 	private Long authorId = 1L;
// 	private Long articleId = 99L;
// 	private String content = "content";

//     @BeforeEach
//     void setUp() {
//         objectMapper = new ObjectMapper();
//         commentRequest = CommentRequest.builder()
//             .authorId(authorId)
//             .articleId(articleId)
//             .content(content)
//             .build();
//     }

//     @Test
//     @Transactional
//     @DisplayName("CR: saveAndFindById")
//     void saveAndFindByIdTest() throws Exception {
//         // Given
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         MvcResult result = mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isCreated())
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");

//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.authorId").value(authorId))
//             .andExpect(jsonPath("$.articleId").value(articleId))
//             .andExpect(jsonPath("$.content").value(content));
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: findAll")
//     void findAllTest() throws Exception {
//         // Given
//         Long authorId1 = 2L;
//         Long articleId1 = 98L;
//         String content1 = "content1";
//         CommentRequest commentRequest1 = CommentRequest.builder()
//             .authorId(authorId1)
//             .articleId(articleId1)
//             .content(content1)
//             .build();

//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);
//         String commentRequestJson1 = objectMapper.writeValueAsString(commentRequest1);

//         // When
//         mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isOk());
//         mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson1))
//             .andExpect(status().isOk());
        
//         String responseJson = mockMvc.perform(get("/comment"))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.length()").value(2))
//             .andReturn()
//             .getResponse()
//             .getContentAsString();
        
//         List<Integer> expectedAuthorIds = Arrays.asList(1, 2);
//         List<Integer> expectedArticleIds = Arrays.asList(99, 98);
//         List<String> expectedContents = Arrays.asList(content, content1);
//         List<Integer> jsonAuthorIds = JsonPath.parse(responseJson).read("$..authorId");
//         List<Integer> jsonArticleIds = JsonPath.parse(responseJson).read("$..articleId");
//         List<String> jsonContents = JsonPath.parse(responseJson).read("$..content");

//         // Then
//         assertThat(jsonAuthorIds).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
//         assertThat(jsonArticleIds).containsExactlyInAnyOrderElementsOf(expectedArticleIds);
//         assertThat(jsonContents).containsExactlyInAnyOrderElementsOf(expectedContents);
//     }

//     @Test
//     @Transactional
//     @DisplayName("findAllByArticleId")
//     void findAllByArticleIdTest() throws Exception {
//         // Given
//         Long authorId1 = 2L;
//         Long articleId1 = 98L;
//         String content1 = "content1";
//         CommentRequest commentRequest1 = CommentRequest.builder()
//             .authorId(authorId1)
//             .articleId(articleId1)
//             .content(content1)
//             .build();

//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);
//         String commentRequestJson1 = objectMapper.writeValueAsString(commentRequest1);

//         // When
//         mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isOk());
//         mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson1))
//             .andExpect(status().isOk());
        
//         String responseJson = mockMvc.perform(get("/comment?articleId=99"))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.length()").value(1))
//             .andReturn()
//             .getResponse()
//             .getContentAsString();

//         List<Integer> expectedAuthorIds = Arrays.asList(1);
//         List<Integer> expectedArticleIds = Arrays.asList(99);
//         List<String> expectedContents = Arrays.asList(content);
//         List<Integer> jsonAuthorIds = JsonPath.parse(responseJson).read("$..authorId");
//         List<Integer> jsonArticleIds = JsonPath.parse(responseJson).read("$..articleId");
//         List<String> jsonContents = JsonPath.parse(responseJson).read("$..content");

//         // Then
//         assertThat(jsonAuthorIds).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
//         assertThat(jsonArticleIds).containsExactlyInAnyOrderElementsOf(expectedArticleIds);
//         assertThat(jsonContents).containsExactlyInAnyOrderElementsOf(expectedContents);
//     }
// }
