package com.example.go4lunch.ui.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.place_autocomplete.PlacePredictionModel;
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
public class MapViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private MapViewModel mMapViewModel;

    public double mLat;
    public double mLng;
    public int mRadius;
    public String mPlaceAutocompleteInput;

    @Mock
    public PlaceRepository mPlaceDataSource;
    public UserRepository mUserDataSource;

    @Before
    public void setUp() throws Exception {
        mLat = 48.8471421;
        mLng = 2.4295305;
        mRadius = 2000;

        mMapViewModel = new MapViewModel(mPlaceDataSource,mUserDataSource);

        mPlaceAutocompleteInput = "Le ruisseau";
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadNearbyPlacesWithSuccess() throws Exception {
        //Given
        MutableLiveData<List<NearbyPlaceModel>> mutableNearbyPlacesList = Mockito.mock(MutableLiveData.class);

        String placeDumpFile = ResourceUtils.getResourceContent(this, "nearbyPlacesList_OneResult.json");
        Gson gson = new Gson();
        List<NearbyPlaceModel> nearbyPlacesList = new ArrayList<>();
        nearbyPlacesList.add(gson.fromJson(placeDumpFile, NearbyPlaceModel.class));

        mutableNearbyPlacesList.setValue(nearbyPlacesList);

        //When
        when(mPlaceDataSource.readNearbyPlaces(mLat,mLng,mRadius)).thenReturn(mutableNearbyPlacesList);

        //Then
        assertNotNull(mMapViewModel.loadNearbyPlaces(mLat,mLng,mRadius));
        Mockito.verify(mutableNearbyPlacesList).setValue(nearbyPlacesList);
    }

    @Test
    public void loadPlacesPredictionWithSuccess() throws Exception {
        //Given
        MutableLiveData<List<PlacePredictionModel>> mutablePlacePredictionsList = Mockito.mock(MutableLiveData.class);

        String predictionDumpFile = ResourceUtils.getResourceContent(this, "placePrediction_OneResult.json");
        Gson gson = new Gson();
        List<PlacePredictionModel> placePredictionsList = new ArrayList<>();
        placePredictionsList.add(gson.fromJson(predictionDumpFile, PlacePredictionModel.class));

        mutablePlacePredictionsList.setValue(placePredictionsList);

        //When
        when(mPlaceDataSource.readPlacesPrediction(mPlaceAutocompleteInput,mLat,mLng,mRadius)).thenReturn(mutablePlacePredictionsList);

        //Then
        assertNotNull(mMapViewModel.loadPlacesPrediction(mPlaceAutocompleteInput,mLat,mLng,mRadius));
        Mockito.verify(mutablePlacePredictionsList).setValue(placePredictionsList);
    }

}