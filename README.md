# docx-template-filler

Web service to fill "variables" in .docx file with given values. Variable here is a text between the given symbols.

## API

Endpoint: `http://localhost:38080/fill`

Response Content-Type: `application/octet-stream`

Request Content-Type: `multipart/form-data`

Request Body:

|         | Type           | Description            |
| ------------- |-------------| ---------------------|
| `template`     | .docx | file to fill |
| `fillData`     | String | JSON data * |

*fillData JSON:

| Field        | Type           | Description            |
| ------------- |-------------| ---------------------|
| `variables`      | array of JSON variable objects** | filds names and values |
| `openingTag`     | String, length = 1 | symbol, that mark the beginning of field to fill with value |
| `closingTag`     | String, length = 1 | symbol, that mark the end of field to fill with value |
| `resultFilename`     | String, optional | name of the resulting file. The default response filename is "filled" + template filename. |

example:

```
{
	"variables": [
		{
			"name": "number",
			"value": "707"
		},
		{
			"name": "date",
			"value": "12.09.2003"
		}
	],
	"openingTag": "{",
	"closingTag": "}",
	"resultFilename": "invoice 707 12.09.2003.docx"
}
```

## Settings

Default port is `38038`. You can change port in application.yaml or with env variable `quarkus.http.port`

## Running the application in dev mode

This project uses Quarkus.

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```
on Windows:
```
mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `docx-template-filler-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/docx-template-filler-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/docx-template-filler-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.