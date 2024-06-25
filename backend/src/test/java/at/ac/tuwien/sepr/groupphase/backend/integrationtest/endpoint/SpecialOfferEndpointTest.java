package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.SpecialOfferMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SpecialOfferEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SpecialOfferRepository repository;

    @Autowired
    private SpecialOfferMapper mapper;

    @Test
    @Transactional

    public void givenNothing_whenCreateSpecialOffer_then201() throws Exception {
        String body = objectMapper.writeValueAsString(TEST_SPECIAL_OFFER_CREATE_DTO_1);
        MvcResult mvcResult = mockMvc.perform(post(SPECIAL_OFFER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", TEST_SPECIAL_OFFER_NAME_1)
                .param("pricePerPax", String.valueOf(TEST_SPECIAL_OFFER_PRICE_PER_PAX_1))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(201, statusCode);
    }

    @Test
    @Transactional
    public void givenNothing_whenGetAllSpecialOffers_thenReturnOffers() throws Exception {
        SpecialOffer specialOffer1 = mapper.specialOfferCreateDtoToSpecialOffer(TEST_SPECIAL_OFFER_CREATE_DTO_1);
        SpecialOffer specialOffer2 = mapper.specialOfferCreateDtoToSpecialOffer(TEST_SPECIAL_OFFER_CREATE_DTO_2);
        repository.save(specialOffer1);
        repository.save(specialOffer2);

        MvcResult mvcResult = mockMvc.perform(get(SPECIAL_OFFER_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<SpecialOfferListDto> specialOffers = objectMapper.readValue(content, new TypeReference<List<SpecialOfferListDto>>() {
        });

        assertAll(
            () -> assertEquals(7, specialOffers.size()),
            () -> assertEquals(TEST_SPECIAL_OFFER_NAME_1, specialOffers.get(0).getName()),
            () -> assertEquals(TEST_SPECIAL_OFFER_PRICE_PER_PAX_1, specialOffers.get(0).getPricePerPax()),
            () -> assertEquals(TEST_SPECIAL_OFFER_NAME_2, specialOffers.get(1).getName()),
            () -> assertEquals(TEST_SPECIAL_OFFER_PRICE_PER_PAX_2, specialOffers.get(1).getPricePerPax())
        );
    }

    @Test
    @Transactional
    public void givenId_whenGetSpecialOffer_thenReturnOffer() throws Exception {
        SpecialOffer specialOffer = mapper.specialOfferCreateDtoToSpecialOffer(TEST_SPECIAL_OFFER_CREATE_DTO_1);
        SpecialOffer savedSpecialOffer = repository.save(specialOffer);

        MvcResult mvcResult = mockMvc.perform(get(SPECIAL_OFFER_URI + "/detail")
                .param("id", savedSpecialOffer.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        SpecialOfferDetailDto specialOfferDetailDto = objectMapper.readValue(content, SpecialOfferDetailDto.class);

        assertAll(
            () -> assertEquals(TEST_SPECIAL_OFFER_NAME_1, specialOfferDetailDto.getName()),
            () -> assertEquals(TEST_SPECIAL_OFFER_PRICE_PER_PAX_1, specialOfferDetailDto.getPricePerPax())
        );
    }

    @Test
    @Transactional
    public void givenValidId_whenDeleteSpecialOffer_thenDelete() throws Exception {
        SpecialOffer specialOffer = mapper.specialOfferCreateDtoToSpecialOffer(TEST_SPECIAL_OFFER_CREATE_DTO_1);
        SpecialOffer saved = repository.save(specialOffer);

        MvcResult mvcResult = mockMvc.perform(delete(SPECIAL_OFFER_URI)
                .content(saved.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(204, statusCode);
    }
}