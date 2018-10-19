package com.sp.service;


/**
 * Supplier
 * @author hejq
 * @date 2018-07-31 8:56
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Supplier是jdk1.8的接口，这里和lamda一起使用了
     *
     * @return
     */
    T get();
}

class Car {
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }
}

class NewFunction {

}
