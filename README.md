# EE Android Realm Browser

Realm Browser provides ability to reviewing multiple local Android Realm DB in separate Activity


# Requirements

- Realm gradle plugin version 3.X.X


# Integration

## Add this into project build.gradle file:
```
allprojects {
    repositories {
        jcenter()

        // Add these line
        maven { url "http://maven.indeema.com:8081/artifactory/libs-release" }
    }
}
```

## Add this into application build.gradle file:
```
dependencies {
    implementation 'com.indeema.libs:realmbrowser:1.0.0'
}
```

## Example of initializing RealmBrowser 
Initialize in your splash/start Activity.

### - Version 1.0.0 and above
```
//Realm browser theme(Theme.DEFAULT or Theme.DRACULA), by default Theme.DEFAULT
int theme = Theme.DRACULA; 

//Realm browser page size, by default 20
int pageSize = 30;

// configuration of 1 of 2 local Realm DB with adding target RealmObject classes
RealmConfiguration configuration1; //must be configuration that is used in app
RbEntity rbEntity1 = new RbEntity(configuration1, UserRealm.class);

// configuration of 2 of 2 local Realm DB with using all existing RealmObject classes
RealmConfiguration configuration2; //must be configuration that is used in app
RbEntity rbEntity2 = new RbEntity(configuration2);

RealmBrowser.initialize(this, theme, pageSize, rbEntity1, rbEntity2);
```

# Boost Version History

## Version 1.0.0

- Created Realm Browser Library with base functionality
- Edit object
- Delete object
- Nested object viewing
- Sorting objects list by field
- Objects list pagination 
- Realm Browser theme
