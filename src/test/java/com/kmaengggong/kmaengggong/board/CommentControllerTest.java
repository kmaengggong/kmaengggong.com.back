// package com.kmaengggong.kmaengggong.board;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
// import com.kmaengggong.kmaengggong.board.interfaces.dto.ArticleRequest;
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
// 	private Long articleId;
// 	private String content = "content";

//     private ArticleRequest articleRequest;
// 	private String title = "title";
// 	private String headerImage = "headerImage";

//     @BeforeEach
//     void setUp() throws Exception {
//         objectMapper = new ObjectMapper();

//         articleRequest = ArticleRequest.builder()
//             .authorId(authorId)
//             .title(title)
//             .content(content)
//             .headerIamge(headerImage)
//             .build();
//         String articleRequestJson = objectMapper.writeValueAsString(articleRequest);
//         MvcResult result = mockMvc.perform(post("/board")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(articleRequestJson))
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");
//         articleId = Long.parseLong(uri.substring(uri.lastIndexOf("/")+1));

//         commentRequest = CommentRequest.builder()
//             .authorId(authorId)
//             .articleId(articleId)
//             .content(content)
//             .build();
//     }

//     String saveFunc(String uri, String requestJson) throws Exception {
//         MvcResult result = mockMvc.perform(post(uri)
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(requestJson))
//             .andExpect(status().isCreated())
//             .andReturn();
//         return result.getResponse().getHeader("Location");
//     }

//     @Test
//     @Transactional
//     @DisplayName("CR: saveAndFindById")
//     void saveAndFindByIdTest() throws Exception {
//         // Given
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         String uri = saveFunc( "/comment", commentRequestJson);

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

//     @Test
//     @Transactional
//     @DisplayName("findAllByArticleIdNotExistsArticle")
//     void findAllByArticleIdNotExistsArticleTest() throws Exception {

//     }

//     @Test
//     @Transactional
//     @DisplayName("R: findByIdNotExists")
//     void findByIdNotExistsTest() throws Exception {
//         // Given
//         String uri = "/comment/9999";

//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: update")
//     void updateTest() throws Exception {
//         // Given
//         String updateContent = "updateContent";
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         MvcResult result = mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isCreated())
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");

//         CommentRequest commentRequestUpdate = CommentRequest.builder()
//             .content(updateContent)
//             .build();
//         String commentRequestJsonUpdate = objectMapper.writeValueAsString(commentRequestUpdate);

//         mockMvc.perform(patch(uri)
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJsonUpdate))
//             .andExpect(status().isNoContent());
        
//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.content").value(updateContent));
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: updateNotExists")
//     void updateNotExistsTest() throws Exception {
//         // Given
//         String updateContent = "updateContent";
//         String uri = "/comment/9999";

//         // When
//         CommentRequest commentRequestUpdate = CommentRequest.builder()
//             .content(updateContent)
//             .build();
//         String commentRequestJsonUpdate = objectMapper.writeValueAsString(commentRequestUpdate);

//         // Then
//         mockMvc.perform(patch(uri)
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJsonUpdate))
//             .andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: updateNullValue")
//     void updateNullValueTest() throws Exception {
//         // Given
//         String updateContent = null;
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         MvcResult result = mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isCreated())
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");

//         CommentRequest commentRequestUpdate = CommentRequest.builder()
//             .content(updateContent)
//             .build();
//         String commentRequestJsonUpdate = objectMapper.writeValueAsString(commentRequestUpdate);

//         mockMvc.perform(patch(uri)
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJsonUpdate))
//             .andExpect(status().isNoContent());
        
//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.content").value(content));
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: deleteById")
//     void deleteByIdTest() throws Exception {
//         // Given
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         MvcResult result = mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isOk())
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");

//         mockMvc.perform(delete(uri))
//             .andExpect(status().isNoContent());
        
//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: deleteByIdNotExists")
//     void deleteByIdNotExistsTest() throws Exception {
//         // Given
//         String uri = "/comment/9999";

//         // Then
//         mockMvc.perform(delete(uri))
//             .andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: likeCommentAndGetCommentLikes")
//     void likeCommentAndGetCommentLikesTest() throws Exception {
//         // Given
//         String commentRequestJson = objectMapper.writeValueAsString(commentRequest);

//         // When
//         MvcResult result = mockMvc.perform(post("/comment")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(commentRequestJson))
//             .andExpect(status().isOk())
//             .andReturn();
//         String uri = result.getResponse().getHeader("Location");
//         uri += "/like";

//         // Then

//     }

//     @Test
//     @Transactional
//     @DisplayName("R: likeCommentAndGetCommentLikesNotExistsComment")
//     void likeCommentAndGetCommentLikesNotExistsCommentTest() throws Exception {
//         // Given
//         String uri = "/comment/9999/like";

//         // Then
//         mockMvc.perform(get(uri))
//             .andExpect(status().isNotFound());

//         mockMvc.perform(post(uri))
//             .andExpect(status().isNotFound());
        
//     }

//     @Test
//     @Transactional
//     @DisplayName("R: likeCommentAndGetCommentLikesDuplicated")
//     void likeCommentAndGetCommentLikesDuplicatedTest() throws Exception {
//         // Given
        
//     }
// }
