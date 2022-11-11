package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagesUrlDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagesUrlDTO.class);
        ImagesUrlDTO imagesUrlDTO1 = new ImagesUrlDTO();
        imagesUrlDTO1.setId(1L);
        ImagesUrlDTO imagesUrlDTO2 = new ImagesUrlDTO();
        assertThat(imagesUrlDTO1).isNotEqualTo(imagesUrlDTO2);
        imagesUrlDTO2.setId(imagesUrlDTO1.getId());
        assertThat(imagesUrlDTO1).isEqualTo(imagesUrlDTO2);
        imagesUrlDTO2.setId(2L);
        assertThat(imagesUrlDTO1).isNotEqualTo(imagesUrlDTO2);
        imagesUrlDTO1.setId(null);
        assertThat(imagesUrlDTO1).isNotEqualTo(imagesUrlDTO2);
    }
}
