package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="route")
public class RouteName {
    @Attribute(name="tag")
    public String routeTag;

    @Attribute(name="title")
    public String routeTitle;
}
