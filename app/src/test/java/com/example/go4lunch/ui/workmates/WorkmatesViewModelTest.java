package com.example.go4lunch.ui.workmates;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.utils.ResourceUtils;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeroSo94 on 25/05/2022.
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private WorkmatesViewModel mWorkmatesViewModel;

    @Mock
    public UserRepository mUserDataSource;

    @Before
    public void setUp() throws Exception {
        mWorkmatesViewModel = new WorkmatesViewModel(mUserDataSource);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadUsers() throws Exception {
        //Given
        MutableLiveData<List<UserModel>> mutableUsersList = Mockito.mock(MutableLiveData.class);

        String userDumpFile = ResourceUtils.getResourceContent(this, "Users_SeveralResults.json");
        Gson gson = new Gson();
        List<UserModel> usersList = new ArrayList<>();
        usersList.add(gson.fromJson(userDumpFile, UserModel.class));

        mutableUsersList.setValue(usersList);

        //When
        when(mUserDataSource.readAllUsersData()).thenReturn(mutableUsersList);

        //Then
        assertNotNull(mWorkmatesViewModel.loadUsers());
        Mockito.verify(mutableUsersList).setValue(usersList);
    }
}