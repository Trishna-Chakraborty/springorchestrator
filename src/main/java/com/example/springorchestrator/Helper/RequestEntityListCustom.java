package com.example.springorchestrator.Helper;

import java.util.ArrayList;
import java.util.List;

public class RequestEntityListCustom<T,I> {

    public List<RequestBodyCustom<T,I>> data;

    public RequestEntityListCustom() {
        this.data = new ArrayList<>();
    }
}
