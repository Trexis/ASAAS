# Any Site as a Service
All functionality is provided using the services application.  The admin interface creates a proxy to the service application and provide a user interface to the administration of the services.


##Administrator Website
Authentication is done using username and password.

###Rest Methods
```
GET: 	/services/{servicesURLtoProxy}
POST:	/services/{servicesURLtoProxy}
{postdata}
```

##Services Website
Authentication is done using username and password.  Pass the following header information with your requests to gain access:
```
Authentication = Basic base64encode(username:password)
```

###Rest Methods
```
GET:	/repository?id={repositoryId}
GET:	/repository?all={true|false}
POST:	/repository
{"name":"{name}", "properties":[]}
```

##Service Library
The services library makes use of Jsoup for proxy of the repository