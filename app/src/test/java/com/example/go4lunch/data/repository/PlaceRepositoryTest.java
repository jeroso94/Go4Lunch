package com.example.go4lunch.data.repository;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.apiservice.PlaceService;
import com.example.go4lunch.data.di.GoogleRetrofitModule;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.place_autocomplete.PlacePredictionModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.example.go4lunch.utils.ResourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Created by JeroSo94 on 27/05/2022.
 */
public class PlaceRepositoryTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    public MockWebServer mMockWebServer;
    public PlaceService mPlaceService;
    public PlaceRepository mPlaceDataSource;

    public double mLat;
    public double mLng;
    public int mRadius;
    public String mNearbySearchType;
    public String mPlaceId;
    public String mPlaceAutocompleteInput;
    public String mPlaceAutocompleteType;

    @Before
    public void setUp() throws Exception {
        mLat = 48.8471421;
        mLng = 2.4295305;
        mRadius = 2000;
        mNearbySearchType = "restaurant";
        mPlaceId = "ChIJMSj7c4Zy5kcRzBLq5t5xask";
        mPlaceAutocompleteInput = "Le ruisseau";
        mPlaceAutocompleteType = "establishment";

        mMockWebServer = new MockWebServer();
        mPlaceService = GoogleRetrofitModule.openRequestChannel(mMockWebServer.url("/"));

        mPlaceDataSource = new PlaceRepository(mPlaceService);
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    @Test
    public void readNearbyPlaces() throws Exception {
        //Given
        String bodyDumpFile = ResourceUtils.getResourceContent(this, "nearbySearch_OneResult.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(bodyDumpFile);
        mMockWebServer.enqueue(response);

        //When
        LiveData<List<NearbyPlaceModel>> liveDataNearbyPlacesList = mPlaceDataSource
                .readNearbyPlaces(mLat,mLng,mRadius);
        List<NearbyPlaceModel> nearbyPlacesList = LiveDataTestUtil.getValue(liveDataNearbyPlacesList);

        //Then
        assertNotNull(nearbyPlacesList);
        assertEquals("ChIJMSj7c4Zy5kcRzBLq5t5xask", nearbyPlacesList.get(0).getPlaceId());
        assertEquals(true, nearbyPlacesList.get(0).getOpeningHours().getOpenNow());
    }

    @Test
    public void readDetailsForPlaceId() throws Exception {
        //Given
        String bodyDumpFile = ResourceUtils.getResourceContent(this, "placeDetailsSearch.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(bodyDumpFile);
        mMockWebServer.enqueue(response);

        //When
        LiveData<PlaceDetailsModel> liveDataPlaceDetails = mPlaceDataSource
                .readDetailsForPlaceId(mPlaceId);
        PlaceDetailsModel placeDetails = LiveDataTestUtil.getValue(liveDataPlaceDetails);

        //Then
        assertNotNull(placeDetails);
        assertEquals("Le Ruisseau Hotel", placeDetails.getName());
    }

    @Test
    public void readPlacesPrediction() throws Exception {
        //Given
        String bodyDumpFile = ResourceUtils.getResourceContent(this, "placePrediction_OneResult.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(bodyDumpFile);
        mMockWebServer.enqueue(response);

        //When
        LiveData<List<PlacePredictionModel>> liveDataPlacePredictionsList = mPlaceDataSource
                .readPlacesPrediction(mPlaceAutocompleteInput,mLat,mLng,mRadius);
        List<PlacePredictionModel> placePredictionsList = LiveDataTestUtil.getValue(liveDataPlacePredictionsList);

        //Then
        assertNotNull(placePredictionsList);
        assertEquals(1, placePredictionsList.size());
        assertEquals("ChIJMSj7c4Zy5kcRzBLq5t5xask", placePredictionsList.get(0).getPlaceId());
    }
}