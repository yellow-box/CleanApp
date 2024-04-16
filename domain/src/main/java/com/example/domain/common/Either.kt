package com.example.domain.common

/**
 * 表示只能是 L 和 R 中的其中一个结果
 * UseCase的调用者通过传递这个不可变函数（onResult），实际上确定了所需的行为
 */
sealed class Either<out L, out R> {
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Either.Left -> fnL(a)
            is Either.Right -> fnR(b)
        }

//    fun <T> flatMap(fnL: (L) -> Any, fnR: (R) -> Any): Any {
//       return when (this) {
//            is Either.Left<L> -> fnL(a)
//            is Either.Right<R> -> fnR(b)
//        }
//    }

//    fun <T> map(fnL: (R)->(T)): Either<L,T> {
//        return when (this) {
//            is Either.Left<L> -> fnL(a)
//            is Either.Right<R> -> fnR(b)
//        }
//    }
}