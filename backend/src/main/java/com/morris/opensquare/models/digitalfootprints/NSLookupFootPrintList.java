package com.morris.opensquare.models.digitalfootprints;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class NSLookupFootPrintList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<NSLookupFootPrint> footPrintList;

    public NSLookupFootPrintList(List<NSLookupFootPrint> list) {
        this.footPrintList = list;
    }

    public List<NSLookupFootPrint> getNsLookupFootPrintList() {
        return this.footPrintList;
    }
}
