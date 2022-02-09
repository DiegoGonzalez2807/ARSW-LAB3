
## Escuela Colombiana de Ingeniería
### Arquitecturas de Software – ARSW


#### Ejercicio – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.

##### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

### 1. Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?

#### Consumo de CPU antes de la ejecución del programa
![img1](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen1.png)

#### Consumo de CPU durante la ejecución del programa 
![img2](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen2.png)

Esta es la gráfica que se obtiene mientras se corre el programa. La primera parte sube exponencialmente debido a que el productor crea 5 objetos de una vez para que el consumidor se lo pueda comer. Apenas el productor inserta en la lista queue el objeto producido, el consumidor saca el objeto que se encuentre en la cola de la lista para consumirlo. Por lo que tenemos una estabilidad en memoria. Respecto a la CPU, este tiene un consumo demasiado alto debido a que el productor debe suplir la necesidad del consumidor. Mientras que el productor crea objetos y descansa 1 segundo, el consumidor no tiene un descanso.

Cabe aclarar que el consumo de CPU durante la ejecución del programa oscila entre el 35% y el 40% de CPU.

![img3](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen3.png)


### 2. Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.

#### Solucion del problema de consumo de CPU
Para el arreglo de este problema de CPU lo que se hace es que el consumidor va a descansar la misma cantidad de tiempo que descansa el productor para crear nuevos objetos.

![img4](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen4.png)

Con este cambio se nota que el consumo de CPU fue reducido casi en su totalidad, esto debido a que el productor puede suplir la necesidad del consumidor y de paso el proceso que hace el consumidor se para por un segundo cada vez que pasa.

![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen5.png)


### 3. Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.

El consumidor va a comer cada 10 milisegundos y el productor va a seguir con el mismo tiempo de espera para producir (1 segundo)
![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen6.png)

#### Consumo de CPU
Como se puede observar, la ejecución del programa alcanza a tener un máximo de 9.3% en consumo de CPU cuando no se tiene un límite en la lista.

![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen7.png)

Lo que nos dice la API de linkedBlockingQueue es que esta es una lista con un máximo ya definido (Integer.MAX_VALUE), sin embargo, hay 3 constructores de esta lista con los cuales le podemos dar un valor definido. Como el productor está creando objetos tan rápido, se dejará un máximo de lista de 3.000 y un mensaje donde se pueda apreciar que si llegamos a esa longitud de cola.

![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen8.png)
![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen10.png)

Efectivamente, se genera un error cuando la longitud de la cola es de 3000
También toca definir en el productor cual es el máximo de stock que se puede ingresar a la cola. En este caso debe ser el mismo valor que el máximo de la lista

![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen11.png)

Se modifica el código del productor, diciendo que en caso de que la longitud de la cola no exceda el máximo definido, siga produciendo objetos, y en caso de que ya llegue al límite, entonces sincronice los hilos para poder parar la inserción de datos en la cola y no generar el error antes mencionado. Esto sirve también para que solo los hilos que estaban produciendo se detengan mientras que los que están consumiendo el objeto sigan

![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen13.png)

#### Consumo de CPU después del cambio
El consumo de CPU del programa se reduce de manera significativa de un máximo de 9.3% a un máximo de 3.6%
![img5](https://github.com/DiegoGonzalez2807/ARSW-LAB3/blob/master/PRIMERA%20PARTE/Images/Imagen9.png)


##### Parte II. – Antes de terminar la clase.

Teniendo en cuenta los conceptos vistos de condición de carrera y sincronización, haga una nueva versión -más eficiente- del ejercicio anterior (el buscador de listas negras). En la versión actual, cada hilo se encarga de revisar el host en la totalidad del subconjunto de servidores que le corresponde, de manera que en conjunto se están explorando la totalidad de servidores. Teniendo esto en cuenta, haga que:

- La búsqueda distribuida se detenga (deje de buscar en las listas negras restantes) y retorne la respuesta apenas, en su conjunto, los hilos hayan detectado el número de ocurrencias requerido que determina si un host es confiable o no (_BLACK_LIST_ALARM_COUNT_).
- Lo anterior, garantizando que no se den condiciones de carrera.

##### Parte III. – Avance para el martes, antes de clase.

Sincronización y Dead-Locks.

![](http://files.explosm.net/comics/Matt/Bummed-forever.png)

1. Revise el programa “highlander-simulator”, dispuesto en el paquete edu.eci.arsw.highlandersim. Este es un juego en el que:

	* Se tienen N jugadores inmortales.
	* Cada jugador conoce a los N-1 jugador restantes.
	* Cada jugador, permanentemente, ataca a algún otro inmortal. El que primero ataca le resta M puntos de vida a su contrincante, y aumenta en esta misma cantidad sus propios puntos de vida.
	* El juego podría nunca tener un único ganador. Lo más probable es que al final sólo queden dos, peleando indefinidamente quitando y sumando puntos de vida.

### 2. Revise el código e identifique cómo se implemento la funcionalidad antes indicada. Dada la intención del juego, un invariante debería ser que la sumatoria de los puntos de vida de todos los jugadores siempre sea el mismo(claro está, en un instante de tiempo en el que no esté en proceso una operación de incremento/reducción de tiempo). Para este caso, para N jugadores, cual debería ser este valor?.

Se tiene una instrucción de creación de inmortales, donde se le pide al usuario que me de la cantidad de luchadores que quiere. Estos se guardan en una lista. En el constructor del ControlFrame, se revisa que en caso que en la lucha no haya ningún inmortal, se pida una lista con nuevos luchadores y estos comiencen a pelear.

En cuestión del código de la clase Immortal.java se revisa que un luchador no pueda pelear consigo mismo a partir de una declaración IF. Se le da a cada luchador la lista de luchadores para que escoja uno para pelear. Sobre la inmortalidad de los luchadores, esta se basa en la función FIGHT, en la cuál se le dice al luchador que si la vida de su contrincante aún no es cero entonces al contrincante se le baja la vida y se le suma a nuestro luchador esa cantidad.

La ecuación que define la totalidad de puntos de vida que debe aparecer es N*100

### 3. Ejecute la aplicación y verifique cómo funcionan las opción ‘pause and check’. Se cumple el invariante?.

El invariante no se cumple debido a que la sumatoria de la vida de los jugadores debería ser 100*N (un ejemplo es que con 3 luchadores la sumatoria debería ser 300) pero se ve en la ejecución del programa que no se cumple

### 4. Una primera hipótesis para que se presente la condición de carrera para dicha función (pause and check), es que el programa consulta la lista cuyos valores va a imprimir, a la vez que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que efectivamente, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Adicionalmente, implemente la opción ‘resume’.

#### Implementación de opción Pause and Check
```java
	synchronized(immortals){
                    for(Immortal immortal:immortals ){
                        immortal.pause();
                    }
                    AtomicInteger sum = new AtomicInteger(0);
                    for (Immortal im : immortals) {
                        synchronized(im.getHealth()){
                            sum.addAndGet(im.getHealth().get());
                        }
                    }   
                    statisticsLabel.setText("<html>"+immortals.toString()+"<br>Health sum:"+ sum);
                }
```
##### Código en Immortal
```java
	 synchronized(immortalsPopulation){
                try{
                    if(paused){
                        immortalsPopulation.wait();
                    }
```
####Implementación botón RESUMEN
```java
	btnResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /**
                 * IMPLEMENTAR
                 */
                synchronized(immortals){
                    for(Immortal immortal:immortals){
                        immortal.resumed();
                    }
                    immortals.notifyAll();
                }

            }
        });
```

##### Código en Immortal
```java
	public void resumed(){
        this.paused = false;
    }
```

	
### 5. Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). Se cumple o no el invariante?.
Se cunmple el invariante

### 6. Identifique posibles regiones críticas en lo que respecta a la pelea de los inmortales. Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:

```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
	}
```

7. Tras implementar su estrategia, ponga a correr su programa, y ponga atención a si éste se llega a detener. Si es así, use los programas jps y jstack para identificar por qué el programa se detuvo.

8. Plantee una estrategia para corregir el problema antes identificado (puede revisar de nuevo las páginas 206 y 207 de _Java Concurrency in Practice_).

9. Una vez corregido el problema, rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante, debe analizar lo realizado en el paso 4.

10. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'inmortales' vivos realizando peleas fallidas con 'inmortales' ya muertos. Es necesario ir suprimiendo los inmortales muertos de la simulación a medida que van muriendo. Para esto:
	* Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'inmortales' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
	* Corrija el problema anterior __SIN hacer uso de sincronización__, pues volver secuencial el acceso a la lista compartida de inmortales haría extremadamente lenta la simulación.

11. Para finalizar, implemente la opción STOP.

<!--
### Criterios de evaluación

1. Parte I.
	* Funcional: La simulación de producción/consumidor se ejecuta eficientemente (sin esperas activas).

2. Parte II. (Retomando el laboratorio 1)
	* Se modificó el ejercicio anterior para que los hilos llevaran conjuntamente (compartido) el número de ocurrencias encontradas, y se finalizaran y retornaran el valor en cuanto dicho número de ocurrencias fuera el esperado.
	* Se garantiza que no se den condiciones de carrera modificando el acceso concurrente al valor compartido (número de ocurrencias).


2. Parte III.
	* Diseño:
		- Coordinación de hilos:
			* Para pausar la pelea, se debe lograr que el hilo principal induzca a los otros a que se suspendan a sí mismos. Se debe también tener en cuenta que sólo se debe mostrar la sumatoria de los puntos de vida cuando se asegure que todos los hilos han sido suspendidos.
			* Si para lo anterior se recorre a todo el conjunto de hilos para ver su estado, se evalúa como R, por ser muy ineficiente.
			* Si para lo anterior los hilos manipulan un contador concurrentemente, pero lo hacen sin tener en cuenta que el incremento de un contador no es una operación atómica -es decir, que puede causar una condición de carrera- , se evalúa como R. En este caso se debería sincronizar el acceso, o usar tipos atómicos como AtomicInteger).

		- Consistencia ante la concurrencia
			* Para garantizar la consistencia en la pelea entre dos inmortales, se debe sincronizar el acceso a cualquier otra pelea que involucre a uno, al otro, o a los dos simultáneamente:
			* En los bloques anidados de sincronización requeridos para lo anterior, se debe garantizar que si los mismos locks son usados en dos peleas simultánemante, éstos será usados en el mismo orden para evitar deadlocks.
			* En caso de sincronizar el acceso a la pelea con un LOCK común, se evaluará como M, pues esto hace secuencial todas las peleas.
			* La lista de inmortales debe reducirse en la medida que éstos mueran, pero esta operación debe realizarse SIN sincronización, sino haciendo uso de una colección concurrente (no bloqueante).

	

	* Funcionalidad:
		* Se cumple con el invariante al usar la aplicación con 10, 100 o 1000 hilos.
		* La aplicación puede reanudar y finalizar(stop) su ejecución.
		
		-->

<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br />Este contenido hace parte del curso Arquitecturas de Software del programa de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería, y está licenciado como <a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">Creative Commons Attribution-NonCommercial 4.0 International License</a>.
