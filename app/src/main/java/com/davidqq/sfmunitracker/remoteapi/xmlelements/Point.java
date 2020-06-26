package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="point")
public class Point {
    @Attribute(name="lat")
    public String lat;

    @Attribute(name="lon")
    public String lon;
}