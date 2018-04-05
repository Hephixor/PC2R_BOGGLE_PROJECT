# Introduction
> **What is Memcached?**
>
> Free & open source, high-performance, distributed memory object caching system, generic in nature, but intended for use in speeding up dynamic web applications by alleviating database load.
>
> Memcached is an in-memory key-value store for small chunks of arbitrary data (strings, objects) from results of database calls, API calls, or page rendering.
>
> Memcached is simple yet powerful.
> Its simple design promotes quick deployment, ease of development, and solves many problems facing large data caches. Its API is available for most popular languages.
>
> *Source: [https://memcached.org/](https://memcached.org/)*

Brièvement, Memcached permet de stocker des données directement en RAM sous la forme clé-valeur .<br>
Ceci peut être par exemple un resultat d'une requête SQL mise en cache, des sessions, des données d'échange rapide entre programme, etc...

# Entrée
Une entrée Memcached est une donnée enregistré.<br>
Cette donnée peut être mis à jour, supprimé ou récupéré.

Les entrées sont manipulable par des commandes Memcached.

## iMap Contacts
Le projet **iMap Contacts** utilise Memcached, afin de stocker l'ID de la session, et les coordonnées de géolocalisation d'un utilisateur, sous la forme:

| Clé            | Valeur                        |
| -------------- | ----------------------------- |
| ID_UTILISATEUR | ID_SESSION;latitude,longitude |

* `ID_UTILISATEUR`      : est l'identifiant unique d'un utilisateur.
* `ID_SESSION`          : est l'identifiant unique d'une session.
* `latitude,longitude`  : sont les coordonnées de géolocalisation rafraichi avec la session.

# Commandes
Les détails des commandes sont accéssible via: [https://github.com/memcached/memcached/wiki/Commands](https://github.com/memcached/memcached/wiki/Commands) .

# Liens
* [Site officiel](https://memcached.org/)
* [Github de Memcached](https://github.com/memcached/memcached)
