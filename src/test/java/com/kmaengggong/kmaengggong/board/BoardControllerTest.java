package com.kmaengggong.kmaengggong.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.kmaengggong.kmaengggong.board.interfaces.dto.ArticleRequest;
import com.kmaengggong.kmaengggong.common.CommonTest;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BoardControllerTest extends CommonTest {
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;
	private ArticleRequest articleRequest;

	private Long authorId = 1L;
	private String title = "title";
	private String content = "content";
	private String headerImage = "headerImage";
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		articleRequest = ArticleRequest.builder()
			.authorId(authorId)
			.title(title)
			.content(content)
			.headerIamge(headerImage)
			.build();
	}

	@Test
	@Transactional
	@DisplayName("CR: saveAndFindById")
	void saveAndFindByIdTest() throws Exception {
		// Given
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.articleRequest.authorId").value(authorId))
			.andExpect(jsonPath("$.articleRequest.title").value(title))
			.andExpect(jsonPath("$.articleRequest.content").value(content))
			.andExpect(jsonPath("$.articleRequest.headerImage").value(headerImage))
			.andExpect(jsonPath("$.commentRequests.length()").value(0));
	}

	@Test
	@Transactional
	@DisplayName("R: findAllPage")
	void findAllPageTest() throws Exception {
		// Given
		Long authorId1 = 2L;
		String title1 = "title1";
		String content1 = "content1";
		String headerImage1 = "headerImage1";
		ArticleRequest articleRequest1 = ArticleRequest.builder()
			.authorId(authorId1)
			.title(title1)
			.content(content1)
			.headerIamge(headerImage1)
			.build();

		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);
		String articleRequestJson1 = objectMapper.writeValueAsString(articleRequest1);

		// When
		mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated());
		mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson1))
			.andExpect(status().isCreated());

		String responseJson = mockMvc.perform(get("/board"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<Integer> expectedAuthorIds = Arrays.asList(1, 2);
		List<String> expectedTitles = Arrays.asList(title, title1);
		List<String> expectedContents = Arrays.asList(content, content1);
		List<String> expectedHeaderImages = Arrays.asList(headerImage, headerImage1);
		List<Integer> jsonAuthorIds = JsonPath.parse(responseJson).read("$..authorId");
		List<String> jsonTitles = JsonPath.parse(responseJson).read("$..title");
		List<String> jsonContents = JsonPath.parse(responseJson).read("$..content");
		List<String> jsonHeaderImages = JsonPath.parse(responseJson).read("$..headerImage");

		// Then
		assertThat(jsonAuthorIds).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
		assertThat(jsonTitles).containsExactlyInAnyOrderElementsOf(expectedTitles);
		assertThat(jsonContents).containsExactlyInAnyOrderElementsOf(expectedContents);
		assertThat(jsonHeaderImages).containsExactlyInAnyOrderElementsOf(expectedHeaderImages);
	}

	@Test
	@Transactional
	@DisplayName("R: findAllSortedPage")
	void findAllSortedPageTest() throws Exception {
		// Given
		Long authorId1 = 2L;
		String title1 = "title1";
		String content1 = "content1";
		String headerImage1 = "headerImage1";
		ArticleRequest articleRequest1 = ArticleRequest.builder()
			.authorId(authorId1)
			.title(title1)
			.content(content1)
			.headerIamge(headerImage1)
			.build();

		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);
		String articleRequestJson1 = objectMapper.writeValueAsString(articleRequest1);

		// When
		mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated());
		mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson1))
			.andExpect(status().isCreated());

		String responseJson = mockMvc.perform(get("/board?sort=createdAt,desc"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<Integer> expectedAuthorIds = Arrays.asList(2, 1);
		List<String> expectedTitles = Arrays.asList(title1, title);
		List<String> expectedContents = Arrays.asList(content1, content);
		List<String> expectedHeaderImages = Arrays.asList(headerImage1, headerImage);
		List<Integer> jsonAuthorIds = JsonPath.parse(responseJson).read("$..authorId");
		List<String> jsonTitles = JsonPath.parse(responseJson).read("$..title");
		List<String> jsonContents = JsonPath.parse(responseJson).read("$..content");
		List<String> jsonHeaderImages = JsonPath.parse(responseJson).read("$..headerImage");

		// Then
		assertThat(jsonAuthorIds).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
		assertThat(jsonTitles).containsExactlyInAnyOrderElementsOf(expectedTitles);
		assertThat(jsonContents).containsExactlyInAnyOrderElementsOf(expectedContents);
		assertThat(jsonHeaderImages).containsExactlyInAnyOrderElementsOf(expectedHeaderImages);
	}

	@Test
	@Transactional
	@DisplayName("R: findAllNotExistsPage")
	void findAllNotExistsPageTest() throws Exception {
		// Given
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated());
		
		// Then
		String responseJson = mockMvc.perform(get("/board?page=9999&size=1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<Integer> expectedAuthorIds = Arrays.asList(1);
		List<String> expectedTitles = Arrays.asList(title);
		List<String> expectedContents = Arrays.asList(content);
		List<String> expectedHeaderImages = Arrays.asList(headerImage);
		List<Integer> jsonAuthorIds = JsonPath.parse(responseJson).read("$..authorId");
		List<String> jsonTitles = JsonPath.parse(responseJson).read("$..title");
		List<String> jsonContents = JsonPath.parse(responseJson).read("$..content");
		List<String> jsonHeaderImages = JsonPath.parse(responseJson).read("$..headerImage");

		// Then
		assertThat(jsonAuthorIds).containsExactlyInAnyOrderElementsOf(expectedAuthorIds);
		assertThat(jsonTitles).containsExactlyInAnyOrderElementsOf(expectedTitles);
		assertThat(jsonContents).containsExactlyInAnyOrderElementsOf(expectedContents);
		assertThat(jsonHeaderImages).containsExactlyInAnyOrderElementsOf(expectedHeaderImages);
	}

	@Test
	@DisplayName("R: findByIdNotExists")
	void findByIdNotExistsTest() throws Exception {
        // Given
        String uri = "/board/9999";

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("R: update")
	void updateTest() throws Exception {
		// Given
		String updateTitle = "updateTitle";
		String updateContent = "updateContent";
		String updateHeaderImage = "updateHeaderImage";
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");

		ArticleRequest articleRequestUpdate = ArticleRequest.builder()
			.title(updateTitle)
			.content(updateContent)
			.headerIamge(updateHeaderImage)
			.build();
		String articleRequestJsonUpdate = objectMapper.writeValueAsString(articleRequestUpdate);

		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJsonUpdate))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.articleRequest.title").value(updateTitle))
			.andExpect(jsonPath("$.articleRequest.content").value(updateContent))
			.andExpect(jsonPath("$.articleRequest.headerImage").value(updateHeaderImage))
			.andExpect(isUpdatedAtAfterCreatedAt("$.articleRequest.createdAt", "$.articleRequest.updatedAt"));
	}

	@Test
	@Transactional
	@DisplayName("R: updateNotExits")
	void updateNotExitsTest() throws Exception {
		// Given
		String updateTitle = "updateTitle";
		String updateContent = "updateContent";
		String updateHeaderImage = "updateHeaderImage";
		String uri = "/board/9999";

		// When
		ArticleRequest articleRequestUpdate = ArticleRequest.builder()
			.title(updateTitle)
			.content(updateContent)
			.headerIamge(updateHeaderImage)
			.build();
		String articleRequestJsonUpdate = objectMapper.writeValueAsString(articleRequestUpdate);

		// Then
		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJsonUpdate))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("R: updateNullValue")
	void updateNullValueTest() throws Exception {
		// Given
		String updateTitle = null;
		String updateContent = null;
		String updateHeaderImage = null;
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");

		ArticleRequest articleRequestUpdate = ArticleRequest.builder()
			.title(updateTitle)
			.content(updateContent)
			.headerIamge(updateHeaderImage)
			.build();
		String articleRequestJsonUpdate = objectMapper.writeValueAsString(articleRequestUpdate);

		mockMvc.perform(patch(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJsonUpdate))
			.andExpect(status().isNoContent());

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.articleRequest.title").value(title))
			.andExpect(jsonPath("$.articleRequest.content").value(content))
			.andExpect(jsonPath("$.articleRequest.headerImage").value(headerImage));
	}

	@Test
	@Transactional
	@DisplayName("R: deleteById")
	void deleteByIdTest() throws Exception {
		// Given
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
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
	@DisplayName("R: deleteByIdNotExists")
	void deleteByIdNotExistsTest() throws Exception {
		// Given
		String uri = "/board/9999";

		// Then
		mockMvc.perform(delete(uri))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("CR: likeArticleAndGetArticleLikes")
	void likeArticleAndGetArticleLikesTest() throws Exception {
		// Given
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");
		uri += "/like";

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(content().json("0"));

		mockMvc.perform(post(uri))
			.andExpect(status().isOk());

		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(content().json("1"));
	}

	@Test
	@Transactional
	@DisplayName("CR: likeArticleAndGetArticleLikesNotExistArticle")
	void likeArticleAndGetArticleLikesNotExistArticleTest() throws Exception {
		// Given
		String uri = "/board/9999/like";

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isNotFound());

		mockMvc.perform(post(uri))
			.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	@DisplayName("CR: likeArticleAndGetArticleLikesDuplicated")
	void likeArticleAndGetArticleLikesDuplicatedTest() throws Exception {
		// Given
		String articleRequestJson = objectMapper.writeValueAsString(articleRequest);

		// When
		MvcResult result = mockMvc.perform(post("/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(articleRequestJson))
			.andExpect(status().isCreated())
			.andReturn();
		String uri = result.getResponse().getHeader("Location");
		uri += "/like";

		// Then
		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(content().json("0"));

		mockMvc.perform(post(uri))
			.andExpect(status().isOk());

		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(content().json("1"));

		mockMvc.perform(post(uri))
			.andExpect(status().isBadRequest());

		mockMvc.perform(get(uri))
			.andExpect(status().isOk())
			.andExpect(content().json("1"));	
	}
}