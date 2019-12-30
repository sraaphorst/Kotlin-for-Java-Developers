package taxipark

// You can often start with either passengers OR trips / drivers OR trips.

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    return allDrivers.filter { driver -> trips.none { it.driver == driver } }.toSet()
}

// Better complexity.
fun TaxiPark.findFakeDrivers2(): Set<Driver> {
    //return allDrivers.minus(trips.map{ it.driver })
    return allDrivers - trips.map{ it.driver }
}
/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    return allPassengers.filter { passenger -> trips.count { passenger in it.passengers } >= minTrips }.toSet()
}

fun TaxiPark.findFaithfulPassengers2(minTrips: Int): Set<Passenger> {
    return trips
            // list with passengers appearing as many times as they made trips
            // Use reference for clarity: could be { it.passengers }
            .flatMap(Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { group -> group.size >= minTrips }
            .keys
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    return allPassengers.filter{ p -> trips.count{ t -> t.passengers.contains(p) && t.driver == driver } >= 2}.toSet()
}

fun TaxiPark.findFrequentPassengers2(driver: Driver): Set<Passenger> {
    return trips
            .filter { trip -> trip.driver == driver }
            .flatMap(Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { group -> group.size > 1 }
            .keys
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return allPassengers.filter { p ->
        val trips = trips.filter { t -> p in t.passengers }
        val discountTrips = trips.count{ it.discount?:0.0 > 0.0 }
        discountTrips > trips.size - discountTrips
    }.toSet()
}

fun TaxiPark.findSmartPassengers2(): Set<Passenger> {
    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition { it.discount != null }
    return allPassengers
            .filter{ passenger ->
                tripsWithDiscount.count { passenger in it.passengers } >
                tripsWithoutDiscount.count { passenger in it.passengers }
            }.toSet()
}

// Highlighting the difference between groupBy and associateBy.
// Use associateBy if your key is unique!
fun TaxiPark.findSmartPassengers3(): Set<Passenger> {
    // Overload that takes key selector and also way to fetch value.
    // key passenger is unique, but groupBy does not know that and returns Map<Passenger, List<List<Trip>>>
    val map1: Map<Passenger, List<List<Trip>>> =  allPassengers
            .groupBy( { it }, { p -> trips.filter { t -> p in t.passengers }})

    // Instead, use knowledge that passengers are unique, and use associateBy
    // to get a Map<Passenger, List<Trip>>.
    val map2: Map<Passenger, List<Trip>> = allPassengers
            .associateBy({ it }, { p -> trips.filter { t -> p in t.passengers }})

    // Instead, we can use associate, which just takes one lambda as an argument.
    // We build the map ourselves using to.
    val map3: Map<Passenger, List<Trip>> = allPassengers
            .associate { p -> p to trips.filter { t -> p in t.passengers }}

    return map3.filterValues { group ->
        val (withDiscount, withoutDiscount) = group.partition { it.discount != null }
        withDiscount.size > withoutDiscount.size
    }.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val tripDurations = trips.groupBy { it.duration / 10 }
    val durationBracket = tripDurations.maxBy { it.value.size }?.key ?: return null
    val minDuration = durationBracket * 10
    val maxDuration = minDuration + 9
    return minDuration..maxDuration
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val totalIncome = trips.sumByDouble { it.cost }
    val numDrivers = (allDrivers.size * 0.2).toInt()

    // Calculate the income of the top numDrivers drivers.
    val incomes = allDrivers.map { d ->
        trips.filter { t -> t.driver == d }.sumByDouble(Trip::cost)
    }.sortedDescending()

    return incomes.take(numDrivers).sum() / totalIncome >= 0.8
}
