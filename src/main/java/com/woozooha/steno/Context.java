package com.woozooha.steno;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Context {

    @Builder.Default
    private List<Page> pages = new ArrayList<>();

}
