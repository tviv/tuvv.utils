package com.tuvv.utils.logging

interface IMessageOutput {

    fun send(priority: Int, tag: String, msg: String)
}