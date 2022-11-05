package com.misterh.tech.angela

data class Tag(var tag: String ?= null, var patterns: ArrayList<String> ?= null, var responses: ArrayList<String> ?= null, var context_set: String ?= null)