package com.xebia.coroutines


import io.kotest.matchers.*
import io.kotest.core.spec.style.WordSpec

class CoroutineTest : WordSpec() {
    init {
        "A Coroutine" should {
            "do this or that" {
                (1 * 0) shouldBe 0
            }
        }
    }
}
