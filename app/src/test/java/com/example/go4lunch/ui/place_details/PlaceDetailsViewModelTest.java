package com.example.go4lunch.ui.place_details;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;
import com.example.go4lunch.ui.map.MapViewModel;
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

import java.util.List;

/**
 * Created by JeroSo94 on 25/05/2022.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaceDetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private PlaceDetailsViewModel mPlaceDetailsViewModel;

    public String mPlaceId;

    @Mock
    public PlaceRepository mPlaceDataSource;
    public UserRepository mUserDataSource;

    @Before
    public void setUp() throws Exception {
        mPlaceId = "ChIJMSj7c4Zy5kcRzBLq5t5xask";

        mPlaceDetailsViewModel = new PlaceDetailsViewModel(mPlaceDataSource,mUserDataSource);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadPlaceDetails() throws Exception {
        //Given
        MutableLiveData<PlaceDetailsModel> mutablePlaceDetails = Mockito.mock(MutableLiveData.class);

        String placeDumpFile = ResourceUtils.getResourceContent(this, "placeDetailsSearch.json");
        Gson gson = new Gson();
        PlaceDetailsModel placeDetails = gson.fromJson(placeDumpFile, PlaceDetailsModel.class);

        mutablePlaceDetails.setValue(placeDetails);

        //When
        when(mPlaceDataSource.readDetailsForPlaceId(mPlaceId)).thenReturn(mutablePlaceDetails);

        //Then
        assertNotNull(mPlaceDetailsViewModel.loadPlaceDetails(mPlaceId));
        Mockito.verify(mutablePlaceDetails).setValue(placeDetails);
    }
}