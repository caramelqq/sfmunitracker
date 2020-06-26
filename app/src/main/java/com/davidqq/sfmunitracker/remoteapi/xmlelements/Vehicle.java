package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="vehicle")
public class Vehicle {
    @Attribute(name="id")
    public String id;

    @Attribute(name="routeTag")
    public String routeTag;

    @Attribute(name="dirTag")
    public String dirTag;

    @Attribute(name="lat")
    public String lat;

    @Attribute(name="lon")
    public String lon;

    @Attribute(name="secsSinceReport")
    public String secsSinceReport;

    @Attribute(name="predictable")
    public String predictable;

    @Attribute(name="heading")
    public String heading;

    @Attribute(name="speedKmHr")
    public String speedKmHr;
}