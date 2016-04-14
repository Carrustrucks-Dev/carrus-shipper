package com.carrus.carrusshipper.utils;


import com.carrus.carrusshipper.R;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.fugu.Fugu;
import com.fugu.interfaces.CallBack;

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

        //configure Fugu
        Fugu.init(this, "njd9wng4d0ycwnn3g4d1jm30yig4d27iom5lg4d3", new CallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(String s) {

            }
        });


        // init Flurry
        FlurryAgent.init(this, MY_FLURRY_APIKEY);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Helvetica/Helvetica.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
