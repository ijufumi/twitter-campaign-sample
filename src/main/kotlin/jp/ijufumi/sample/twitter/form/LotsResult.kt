package jp.ijufumi.sample.twitter.form

data class LotsResult (var result: Boolean, var error: Boolean){
    constructor() : this(false, false)
}
