# Pseudocode
Here is a selection of operations that user's will be able to perform. I have written some pseudocode to assist me when it comes to implementation. These represent a high-level overview of how each operation will be carried out.

### Creating a new collection
```
collections = []

newCollection = Collection(name)
collections.add(newCollection)
```

### Getting a collection
```
collections = [...]

if (collections.contains("Luke's LEGO Collection")) then
    return collections["Luke's LEGO Collection"]
else
    return error("Collection not found")
```

### Creating a new set
```
newSet = LegoSet(
    setNumber,
    name,
    pieces,
    price,
    status,
    theme,
    minifigures
)

collection["Luke's LEGO Collection"].addSet(newSet)
```

### Save collection data to storage
```
collections = [...]

jsonCollections = collections.toJSON()

if (not file.exists("data.json")) then
    file.create("data.json")
    
file.get("data.json").set(jsonCollections)
```

### Filter sets by theme
```
collections = [...]
searchTheme = "Technic"

for collection in collections:
    for set in collection:
        if (set.theme == searchTheme)
            print(set.name + " has the theme " + searchTheme)
```