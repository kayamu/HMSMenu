package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagesUrlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagesUrl.class);
        ImagesUrl imagesUrl1 = new ImagesUrl();
        imagesUrl1.setId(1L);
        ImagesUrl imagesUrl2 = new ImagesUrl();
        imagesUrl2.setId(imagesUrl1.getId());
        assertThat(imagesUrl1).isEqualTo(imagesUrl2);
        imagesUrl2.setId(2L);
        assertThat(imagesUrl1).isNotEqualTo(imagesUrl2);
        imagesUrl1.setId(null);
        assertThat(imagesUrl1).isNotEqualTo(imagesUrl2);
    }
}
