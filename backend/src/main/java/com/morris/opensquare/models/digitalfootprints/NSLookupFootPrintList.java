package com.morris.opensquare.models.digitalfootprints;

import java.util.List;

public class NSLookupFootPrintList {
    private final List<NSLookupFootPrint> footPrintList;

    public NSLookupFootPrintList(List<NSLookupFootPrint> list) {
        this.footPrintList = list;
    }

    public List<NSLookupFootPrint> getNsLookupFootPrintList() {
        return this.footPrintList;
    }
}
