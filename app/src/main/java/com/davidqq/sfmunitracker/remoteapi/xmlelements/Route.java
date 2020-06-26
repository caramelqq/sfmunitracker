package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name="route")
public class Route {
    @Attribute(name="tag")
    public String tag;

    @Attribute(name="title")
    public String title;

    @Attribute(name="color")
    public String color;

    @Attribute(name="oppositeColor")
    public String oppositeColor;

    @Attribute(name="latMin")
    public String latMin;

    @Attribute(name="latMax")
    public String latMax;

    @Attribute(name="lonMin")
    public String lonMin;

    @Attribute(name="lonMax")
    public String lonMax;

    @Element
    public List<Stop> stop;

    @Element
    public List<Direction> direction;

    @Element
    public List<Path> path;
}