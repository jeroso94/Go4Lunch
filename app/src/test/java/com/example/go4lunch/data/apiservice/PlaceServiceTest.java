package com.example.go4lunch.data.apiservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.di.GoogleRetrofitModule;
import com.example.go4lunch.model.nearby_search.NearbyResultModel;
import com.example.go4lunch.model.place_autocomplete.PlaceAutocompleteResultModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsResultModel;
import com.example.go4lunch.utils.ResourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

/**
 * Created by JeroSo94 on 24/05/2022.
 */
public class PlaceServiceTest {

    public MockWebServer mMockWebServer;
    public PlaceService mPlaceService;
    public String mLatLng;
    public int mRadius;
    public String mNearbySearchType;
    public String mPlaceId;
    public String mPlaceAutocompleteInput;
    public String mPlaceAutocompleteType;


    @Before
    public void setUp() throws Exception {

        mLatLng = "48.8471421,2.4295305";
        mRadius = 2000;
        mNearbySearchType = "restaurant";
        mPlaceId = "ChIJMSj7c4Zy5kcRzBLq5t5xask";
        mPlaceAutocompleteInput = "Le ruisseau";
        mPlaceAutocompleteType = "establishment";

        mMockWebServer = new MockWebServer();
        mPlaceService = GoogleRetrofitModule.openRequestChannel(mMockWebServer.url("/"));
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    @Test
    public void requestNearbySearchWithSuccess() throws IOException {
        //Given
        String body = ResourceUtils.getResourceContent(this, "nearbySearch_OneResult.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(body);
        mMockWebServer.enqueue(response);

        //When
        Response<NearbyResultModel> results = mPlaceService
                .requestNearbySearch(BuildConfig.PLACES_API_KEY, mLatLng, String.valueOf(mRadius), mNearbySearchType).execute();

        //Then
        assertNotNull(results.body());
        assertEquals("OK", results.body().getStatus());
        assertEquals(1, results.body().getListOfProvidedPlaces().size());
        assertEquals("ChIJMSj7c4Zy5kcRzBLq5t5xask", results.body().getListOfProvidedPlaces().get(0).getPlaceId());
        assertEquals(true, results.body().getListOfProvidedPlaces().get(0).getOpeningHours().getOpenNow());

    }

    @Test
    public void requestPlaceDetailsSearchWithSuccess() throws IOException {
        //Given
        String body = ResourceUtils.getResourceContent(this, "placeDetailsSearch.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(body);
        mMockWebServer.enqueue(response);

        //When
        Response<PlaceDetailsResultModel> result = mPlaceService
                .requestPlaceDetailsSearch(BuildConfig.PLACES_API_KEY, mPlaceId).execute();

        //Then
        assertNotNull(result.body());
        assertEquals("OK", result.body().getStatus());
        assertEquals("Le Ruisseau Hotel", result.body().getPlaceDetails().getName());
    }

    @Test
    public void requestPlacesPrediction() throws IOException{
        //Given
        String body = ResourceUtils.getResourceContent(this, "placePrediction_OneResult.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(body);
        mMockWebServer.enqueue(response);

        //When
        Response<PlaceAutocompleteResultModel> predictions = mPlaceService
                .requestPlacesPrediction(BuildConfig.PLACES_API_KEY, mPlaceAutocompleteInput, mLatLng, String.valueOf(mRadius), mPlaceAutocompleteType).execute();

        //Then
        assertNotNull(predictions.body());
        assertEquals("OK", predictions.body().getStatus());
        assertEquals(1, predictions.body().getPlacesPredictionList().size());
        assertEquals("ChIJMSj7c4Zy5kcRzBLq5t5xask", predictions.body().getPlacesPredictionList().get(0).getPlaceId());
    }
}