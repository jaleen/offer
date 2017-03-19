

package com.jalalsoft.offer;

import com.jalalsoft.offer.dao.OfferDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfferDao offerDao;



    @Test
    public void givenServerStarted_theRootLevelElementsShouldExist() throws Exception {

        mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AA Batteries for sale..\", \"currency\":\"GBP\", \"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andExpect(
                header().string("Location", containsString("offers/")));

        mockMvc.perform(get("/offers")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.[0].price").exists());
    }

    @Test
    public void whenAnOfferReceived_ItShouldCreateAnOffer() throws Exception {

        mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AA Batteries for sale..\", \"currency\":\"GBP\", \"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andExpect(
                header().string("Location", containsString("offers/")));

    }

    @Test
    public void whenAnOfferExist_theShouldRetrieveAnOffer() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AAA batteries for sale.\", \"currency\":\"GBP\",\"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.description").value("AAA batteries for sale.")).andExpect(
                jsonPath("$.currency").value("GBP")).andExpect(
                jsonPath("$.price").value("1.0"));
    }


    @Test
    public void whenPriceIsChanged_thenOfferShouldBeUpdated() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AAA batteries for sale.\", \"currency\":\"gbp\",\"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        //change the price
        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(
                    "{\"description\": \"AAA batteries for sale.\", \"currency\":\"gbp\",\"price\":\"2.0\", \"id\":\"1\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON).content(
                "{\"id\": \"1\"}")).andExpect(
                jsonPath("$.price").value("2.0"));
    }

    @Test
    public void whenAllFieldsAreChanged_ThenAllOfThemAreReflected() throws Exception {


        MvcResult mvcResult = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AAA batteries for sale.\", \"currency\":\"gbp\",\"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        //change the price
        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AA Rechargeable batteries for sale.\", \"currency\":\"USD\",\"price\":\"2.0\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.description").value("AA Rechargeable batteries for sale.")).andExpect(
                jsonPath("$.currency").value("USD")).andExpect(
                jsonPath("$.price").value("2.0"));
    }

    @Test
    public void whenAnOfferIsDeleted_theOfferSouldBeRemoved() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON).content(
                "{\"description\": \"AAA batteries for sale.\", \"currency\":\"gbp\",\"price\":\"1.0\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
    @Before
    public void atStart_thenCleanAll() {
        offerDao.deleteAll();
    }
}