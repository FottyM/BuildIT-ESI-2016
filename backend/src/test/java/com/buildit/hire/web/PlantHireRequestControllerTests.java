package com.buildit.hire.web;

import com.buildit.BuilditApplication;
import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.rest.PlantHireRequestController;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.inventory.application.service.RentalService;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.POStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.hateoas.hal.Jackson2HalModule;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import static org.mockito.Mockito.when;

/**
 * Created by rain on 30.03.16.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BuilditApplication.class})
@WebAppConfiguration
@DirtiesContext
@ActiveProfiles("test-without-mockito")
public class PlantHireRequestControllerTests {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestController plantHireRequestController;
    private MockMvc mockMvc;
    @Autowired
    @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    List<ClientHttpRequestInterceptor> savedInterceptors = null;

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void setup() {
        savedInterceptors = restTemplate.getInterceptors();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void tearoff() {
        restTemplate.setInterceptors(savedInterceptors);
    }


    @Test
    public void testCreatePurchaseOrder() throws PlantNotAvailableException, Exception {

        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=Exc&startDate=2016-03-14&endDate=2016-03-25"))
                .andReturn();


        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() {
                        });
        assertThat(plants.size(), is(2));
//
//        PlantHireRequestDTO hireRequest = new PlantHireRequestDTO();
//        hireRequest.setPlantId(plants.get(1).get_id());
//        hireRequest.setPrice(plants.get(1).getPrice());
//        hireRequest.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));
//
//        MvcResult result2 = mockMvc.perform(post("/api/inventory/plants")
//                .content(mapper.writeValueAsString(hireRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        PlantHireRequestDTO plantHireRequestDTO = mapper.readValue(result2.getResponse().getContentAsString(), PlantHireRequestDTO.class);
//
//
//        MvcResult result3 = mockMvc.perform(post("/api/inventory/plants/phrs/" + plantHireRequestDTO.get_id().toString()))
//                .andReturn();
//        PurchaseOrderDTO prequest = mapper.readValue(result3.getResponse().getContentAsString(),
//                new TypeReference<PurchaseOrderDTO>() {
//                });
//
//
//        assertThat(prequest.getStatus(), is(POStatus.OPEN));


//        PlantInventoryEntryDTO plant = new PlantInventoryEntryDTO();
//        plant.add(new Link("http://rentit.com/api/inventory/plants/13"));
//        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusDays(4)));
//
//        System.out.println( rentalService.createPurchaseOrder(order));;
    }

    @Test
    public void testCreatePurchaseOrderRejection() throws Exception{

        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=Exc&startDate=2016-03-14&endDate=2016-03-25"))
                .andReturn();


        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() {
                        });
        assertThat(plants.size(), is(2));

        PlantHireRequestDTO hireRequest = new PlantHireRequestDTO();
      //  hireRequest.setPlantId(plants.get(1).get_id());
        hireRequest.setPrice(plants.get(1).getPrice());
        hireRequest.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        MvcResult result2 = mockMvc.perform(post("/api/inventory/plants")
                .content(mapper.writeValueAsString(hireRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        PlantHireRequestDTO plantHireRequestDTO = mapper.readValue(result2.getResponse().getContentAsString(), PlantHireRequestDTO.class);


        ClientHttpRequestInterceptor interceptor = new ClientHttpRequestInterceptor() {
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().add("prefer", "409");
                return execution.execute(request, body);
            }
        };
        restTemplate.setInterceptors(Collections.singletonList(interceptor));

        MvcResult result3 = mockMvc.perform(post("/api/inventory/plants/phrs/"+ plantHireRequestDTO.get_id().toString()))
                .andExpect(status().isConflict())
                .andReturn();


        PurchaseOrderDTO rejectedPo = mapper.readValue(result3.getResponse().getContentAsString(), PurchaseOrderDTO.class);

        assertThat(rejectedPo.getStatus(), is(POStatus.REJECTED));

        restTemplate.setInterceptors(savedInterceptors);
        MvcResult result4 = mockMvc.perform(put("/api/inventory/plants")
                .content(mapper.writeValueAsString(rejectedPo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        PurchaseOrderDTO acceptedBeforeRejectedPo = mapper.readValue(result4.getResponse().getContentAsString(), PurchaseOrderDTO.class);
        assertThat(acceptedBeforeRejectedPo.getStatus(), is(POStatus.OPEN));

        ClientHttpRequestInterceptor interceptor2 = new ClientHttpRequestInterceptor() {
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().add("prefer", "409");
                return execution.execute(request, body);
            }
        };
        restTemplate.setInterceptors(Collections.singletonList(interceptor2));

        MvcResult result5 = mockMvc.perform(put("/api/inventory/plants")
                .content(mapper.writeValueAsString(rejectedPo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();

        assertThat(rejectedPo.getStatus(), is(POStatus.REJECTED));








    }

}
