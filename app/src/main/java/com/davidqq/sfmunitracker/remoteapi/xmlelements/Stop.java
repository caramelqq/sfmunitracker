package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="stop")
public class Stop {
    @Attribute(name="tag")
    public String tag;

    @Attribute(name="title")
    public String title;

    @Attribute(name="lat")
    public String lat;

    @Attribute(name="lon")
    public String lon;

    @Attribute(name="stopId")
    public String stopId;
}
