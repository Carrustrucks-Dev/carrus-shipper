package com.carrus.carrusshipper.utils;


import com.carrus.carrusshipper.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Muddassir on 6/10/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Helvetica/Helvetica.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
