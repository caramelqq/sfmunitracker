package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name="body")
public class BusRoute {
    @Attribute(name="copyright")
    public String copyright;

    @Element
    public List<Route> route;
}