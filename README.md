# Auto Import Provider Service

An API service that analyzes Kotlin files for missing imports, provides suggestions based on unresolved references, and resolves missing imports by suggesting candidate classes from the classpath.

---

## Features

- **Unresolved Reference Detection**: Scans Kotlin files for unresolved references and identifies missing imports.
- **Import Suggestions**: Suggests possible candidates for unresolved classes based on available classes in the classpath.
- **Works with Kotlin Code**: Specifically designed to work with Kotlin files and handle their import management.

---

## Project Structure

### 1. `AutoImportProviderService`
The core service that:
- Takes in a Kotlin file (or multiple files) with unresolved references.
- Scans the classpath for matching classes.
- Returns a list of candidate classes to import for each unresolved reference.

### 2. Example Use Case

The service receives a POST request with a Kotlin file and scans the file for unresolved references like `ZipFile`. It then returns suggestions for possible class names that match `ZipFile`, along with the full class names to be imported.

---

## Example Request

### Request Body
This is the format of the request body to the API:

```json
{
  "args": "Some arguments for the compiler",
  "files": [
    {
      "name": "ExampleFile.kt",
      "text": "fun exampleFunction() {\n    val zip = ZipFile() // <- Missing import for ZipFile\n    println(zip)\n}"
    }
  ]
}
```

### Request

POST request to `http://localhost:8080/api/compiler/autoimport`.

---

## Example Response

The API returns a list of suggestions based on the unresolved references in the Kotlin file. For instance:

```json
[
  {
    "name": "ZipFile",
    "candidates": [
      "org.jetbrains.kotlin.com.intellij.ide.plugins.ImmutableZipFileDataLoader",
      "org.jetbrains.kotlin.com.intellij.ide.plugins.JavaZipFileDataLoader",
      "org.jetbrains.kotlin.com.intellij.util.lang.ZipFilePool",
      "java.util.zip.ZipFile",
      "jdk.zipfs.jdk.nio.zipfs.ZipFileSystem"
    ]
  }
]
```

In this example, the unresolved reference `ZipFile` is matched to multiple candidates, and the system suggests all possible class names to import.

---

## Usage

The service can be used to resolve missing imports in Kotlin files programmatically. Here’s a breakdown of the process:

1. **Prepare the Request**: Send a POST request to the endpoint `/api/compiler/autoimport` with the Kotlin code as part of the body.
2. **Process the Response**: The service analyzes the code and returns a list of suggestions for missing imports.

---

## Contributing

Feel free to contribute to this project by:
- Adding support for additional classpath sources or file types.
- Improving the performance of classpath scanning.
- Enhancing the accuracy of unresolved reference detection.

Fork the repository, create a new branch for your changes, and submit a pull request.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- Thanks to the Kotlin community for their powerful tools that make this project possible.
