package com.example.store_app.core

data class ResourceState<T>(
    var resource: Resource<T>? = null,
    var listResource: Resource<List<T>>? = null
)
