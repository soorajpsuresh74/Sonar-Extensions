package com.reflectionsinfos;

import org.sonar.api.Plugin;

public class TooManyParameters implements Plugin{
    @Override
    public void define(Context context){
        context.addExtension(TooManyParametersCheck.class);
    }
}
