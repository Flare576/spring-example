package com.flare576.restCountries.service;

import com.flare576.restCountries.model.Country;

/**
 * Similar to QueryController; if we add many more fields, multiple interfaces may be prudent.
 *
 * Created by Flare576 on 1/20/2016.
 */
public interface QueryService {
    Country[] getCountriesByAlpha3(String codes);

    Country[] getByBorderCount(Integer qty);

    Country[] getByMaxBorderCount();

    Integer getMaxBorderCount();

    Country[] getByTimezoneCount(Integer qty);

    Country[] getByMaxTimezoneCount();

    Integer getMaxTimezoneCount();
}
