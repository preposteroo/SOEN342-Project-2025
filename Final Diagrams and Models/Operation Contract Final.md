**Contract:** makeOffering()

**Operation:** makeOffering(startDate: String, endDate: String, dayOfWeek: EDayOfWeek, startTime: String, endTime: String, offeringMode: EOfferingMode, courseName: String)

**Cross-References:** Use Case 1: Process Offerings

**Preconditions:**
* Admin is authenticated and logged in
* Offering is unique

**Postconditions:**
* An Offering instance *offering* was created (instance creation)
* A Schedule instance *schedule* was created (instance creation)
* *offering.schedule* was set to schedule (attribute modification)

_________________________________________________________________________

**Contract:** takeOffer()

**Operation:** takeOffer(offering: Offering)

**Cross-References:** Use Case 1: Process Offerings

**Preconditions:**
* The instructor is authenticated and logged in
* The offering has not been accepted by another instructor
* The instructor's location matches the offering's location

**Postconditions:**
* *offering* is associated with the instructor (association formed)
* A Lesson instance *lesson* was created (instance creation)
* *lesson.client* was set to client (attribute modification)
* *lesson.offering* was set to offering (attribute modification)

_________________________________________________________________________

**Contract:** makeBooking()

**Operation:** makeBooking(offering: Offering, client: Client)

**Cross-References:** Use Case 2: Process Bookings

**Preconditions:**
* A client is successfully authenticated and logged in
* The lesson exists
* The client does not have other lessons which would overlap

**Postconditions:**
* A Booking instance *booking* was created (instance creation)
* *booking.client* was set to client (attribute modification)
* *lesson.availability* was set to `not available`
