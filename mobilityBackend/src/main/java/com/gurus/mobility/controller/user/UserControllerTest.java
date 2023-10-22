package com.gurus.mobility.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurus.mobility.dto.UserList;
import com.gurus.mobility.entity.user.FileDB;
import com.gurus.mobility.entity.user.User;
import com.gurus.mobility.repository.User.FileDBRepository;
import com.gurus.mobility.repository.User.UserRepository;
import com.gurus.mobility.service.User.IFileStorageService;
import com.gurus.mobility.service.User.ImageService;
import com.gurus.mobility.service.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired(required=false)
    private MockMvc mockMvc;

    @Autowired(required=false)
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FileDBRepository fileDBRepository;

    @MockBean
    private IFileStorageService fileStorageService;

    @Autowired
    private UserController userController;

    @BeforeEach
    public void setUp() {
        User simulatedUser = new User();
        simulatedUser.setId(1L);
        simulatedUser.setUserName("simulatedUser");
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(simulatedUser);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("user1");
        userList.add(user1);
        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserList responseUserList = objectMapper.readValue(jsonResponse, UserList.class);

        assertEquals(1, responseUserList.getUsers().size());
        assertEquals("user1", responseUserList.getUsers().get(0).getUserName());
    }

    @Test
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        User simulatedUser = new User();
        simulatedUser.setId(userId);
        simulatedUser.setUserName("simulatedUser");
        Mockito.when(userService.getUserById(userId)).thenReturn(simulatedUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        User responseUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userId, responseUser.getId());
        assertEquals("simulatedUser", responseUser.getUserName());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setUserName("updatedUser");

        Mockito.when(userService.updateUser(Mockito.any(User.class), Mockito.anyLong())).thenReturn(updateUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        User updatedUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals("updatedUser", updatedUser.getUserName());
    }

    @Test
    public void testActivateUser() throws Exception {
        Mockito.when(userService.Verified(Mockito.anyLong())).thenReturn("Success");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/Activate/1"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertTrue(responseContent.contains("Success"));
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "Test file".getBytes());

        FileDB simulatedImage = new FileDB();
        simulatedImage.setName("test.jpg");

        FileDBRepository fileDBRepositoryMock = Mockito.mock(FileDBRepository.class);

        Mockito.when(fileDBRepositoryMock.findByName(Mockito.anyString())).thenReturn(simulatedImage);

        User simulatedUser = new User();
        simulatedUser.setId(1L);
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(simulatedUser);

        Field imageRepositoryField = UserController.class.getDeclaredField("ImageRepository");
        imageRepositoryField.setAccessible(true);
        imageRepositoryField.set(userController, fileDBRepositoryMock);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/1/uploadimage")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertTrue(jsonResponse.contains("Uploaded the file successfully"));
    }


}
