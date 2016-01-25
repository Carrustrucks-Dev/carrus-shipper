package com.carrus.carrusshipper.utils;


import com.carrus.carrusshipper.R;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.carrus.carrusshipper.utils.Constants.MY_FLURRY_APIKEY;


/**
 * Created by Muddassir on 6/10/15 for CarrusShipper.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // configure Flurry
        FlurryAgent.setLogEnabled(true);

        // init Flurry
        FlurryAgent.init(this, MY_FLURRY_APIKEY);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Helvetica/Helvetica.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
