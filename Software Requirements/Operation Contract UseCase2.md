**Contract:** makeBooking()

**Operation:** makeBooking(offering: Offering, client: Client)

**Cross-References:** Use Case 2: Process Bookings

**Preconditions:**
* A client is succesfully authenticated and logged in
* The offering exists
* The client and the offering don't have any overlap

**Postconditions:**
* A Lesson instance *lesson* was created (instance creation)
* *lesson.client* was set to client (attribute modification)
* *offering.booked* was set to `true`