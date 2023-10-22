    package com.gurus.mobility.controller.user;

    import com.gurus.mobility.service.User.IFileStorageService;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.http.MediaType;
    import org.springframework.mock.web.MockMultipartFile;
    import org.springframework.test.context.ActiveProfiles;
    import org.springframework.test.context.junit4.SpringRunner;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    public class FileControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private IFileStorageService storageService;


        @Test
        public void testUploadFileSuccess() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                    MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
            mockMvc.perform(multipart("/upload")
                            .file(file))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                            .value("Uploaded the file successfully: test.txt"));
        }
    }
