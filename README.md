# Motor de busqueda usando Apache Solr

Este repositorio esta compuesto por dos proyectos, el servidor de Solr (**SearchEngineServer**), y el buscador (**SearchEngine**).
Se ha implentado un motor de busqueda para una coleccion de documentos medicos en ingles.


## Indice
- [SearchEngine](#SearchEngine)
- [SearchEngineServer](#SearchEngineServer)
- [Herramientas Utilizadas](#Herramientas)
- [Instalacion](#Instalacion)

## SearchEngine

Este proyecto es la aplicacion del motor de busqueda. 
Si el servidor no esta encendido la aplicacion se cerrara mostrando un aviso.


## SearchEngineServer

Este proyecto se encarga tanto de iniciar Solr, como de crear y configurar la coleccion que se va a utilizar.

> [!Note]
> Este codigo se ha diseñado de tal forma que no se necesite utilizar tanto la consola de comandos
como la interfaz web de Solr.

> [!Warning]
> El proyecto esta pensado para utilizarse en un entorno de Windows. Si se desea utilizar un entorno de Linux,
habria que realizar alguna pequeña modificacion en la clase [Comandos](./SearchEngineServer/src/main/java/com/modelo/solr/Comandos.java).

## Herramientas utilizadas
- **Java version:** JDK 22
+ **IDE:** Netbeans 21
- **Solrj version:** 8.11.4

## Instalacion

1. Descargar y descomprimir la carpeta de [Apache Solr]*(#https://solr.apache.org/downloads.html).
2. Copia la carpeta en la ubicación `C:\` y renombra la carpeta a 'solr'.
3. Ejecutar SearchEngineServer.
4. Ejecutar SearchEngine.

