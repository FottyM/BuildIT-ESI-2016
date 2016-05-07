FORMAT: 1A
HOST: http://localhost:3000

# ESI14-RentIt
Excerpt of RentIt's API

# Group Purchase Orders
Notes related resources of the **Plants API**

## Plant Catalog [/api/inventory/plants{?name,startDate,endDate}]
### Retrieve Plants [GET]
+ Parameters
    + name (optional,string) ... Name of the plant
    + startDate (optional,date) ... Starting date for rental
    + endDate (optional,date) ... End date for rental

+ Response 200 (application/json)

        [
            {"_id":13,"name":"D-Truck","description":"15 Tonne Articulating Dump Truck","price":250.00,"_links":{"self":{"href":"http://192.168.99.100:3000/api/inventory/plants/13"}}},
            {"_id":14,"name":"D-Truck","description":"30 Tonne Articulating Dump Truck","price":300.00,"_links":{"self":{"href":"http://192.168.99.100:3000/api/inventory/plants/14"}}}
        ]

## Purchase Order - Container [/api/sales/orders]
### Create Purchase Order [POST]
+ Request (application/json)

        {
            "plant":{
                "_links":{"self":{"href":"http://192.168.99.100:3000/api/inventory/plants/13"}}
            },
            "rentalPeriod": {"startDate":"2016-11-12", "endDate":"2016-11-14"}
        }

+ Response 201 (application/json)

    + Headers

            Location: http://192.168.99.100:3000/api/sales/orders/100

    + Body

            {
                "plant": {
                    "name": "D-Truck",
                    "description": "15 Tonne Articulating Dump Truck",
                    "price": 250,
                    "_links": {
                        "self": {"href": "http://192.168.99.100:3000/api/inventory/plants/13"}
                    }
                },
                "rentalPeriod": {"startDate": "2016-11-12", "endDate": "2016-11-14"},
                "status": "OPEN",
                "total": 750,
                "_links": {
                    "self": {"href": "http://192.168.99.100:3000/api/sales/orders/100"}
                },
                "_xlinks": [
                    {
                        "_rel": "extend",
                        "href": "http://192.168.99.100:3000/api/sales/orders/100/extensions",
                        "method": "POST"
                    }
                ]
            }