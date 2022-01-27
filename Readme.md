         _________ __                 __         
        /   _____/|  | __ ___.__.    |__|  ____  
        \_____  \ |  |/ /<   |  |    |  | /  _ \ 
        /        \|    <  \___  |    |  |(  <_> )
       /_______  /|__|_ \ / ____|/\__|  | \____/ 
               \/      \/ \/     \______|        
			  
  
# README


Le principe du projet est de coder le jeu de <br />
carte Skyjo en utilisant  java avec une interfaces <br />
graphique a l' aide de javafxml en respectant le pattern MVC  <br />
et en implementent un observateur observé.<br />
<br />
Skyjo est un jeu dont l’objectif est d’obtenir le moins de<br />
points possibles en inversant et échangeant des cartes. <br />
Chaque joueur possède 12 cartes dont la valeur varie de <br />
-2 à 12. La description des règles du jeu est disponible <br />
via https://www.regledujeu.fr/regle-du-skyjo. <br />
<br />

## Regles du jeu<br />

### Début d’une partie<br />
Lorsqu’une manche démarre, deux cartes ont été retournées <br />
pour chaque joueur. Le joueur avec le plus de points commence. <br />
Si les deux joueurs ont le même nombre de points, choisissez <br />
au hasard. <br />

### Déroulement d’une manche<br />

Le premier joueur doit choisir entre les deux actions suivantes :<br />
- piocher la défausse ;<br />
- retourner une carte de la pioche.<br />

Si le joueur choisit de piocher une carte de la défausse, il doit<br />
échanger la carte défaussée avec une de ses cartes. Il peut <br />
l’échanger contre n’importe laquelle de ses cartes,qu’elle soit <br />
face visible ou face cachée. Lorsqu’il procède à cet échange, il <br />
rend la carte échangée face visible sur la pile de défausse.<br />

Si le joueur choisit de retourner une carte de la pioche, il peut<br />
reposer la carte tirée sur la pile de défausse (face visible) et<br />
retourner une de ses cartes dont la valeur était cachée OU il peut<br />
échanger la carte piochée avec n’importe laquelle de ses cartes.<br />

Lorsqu’il procède à cet échange, le joueur dépose son ancienne carte <br />
face visible sur la défausse.<br />
Après cette action, le nombre de points du joueur est mis à jour et <br />
le joueur suivant peut jouer.<br />

### Fin d’une manche<br />

Une manche se décompose en plusieurs tours. Chaque tour permettant <br />
aux deux joueurs d’effectuer leurs actions. Une manche se termine <br />
lorsqu’un des deux joueurs a retourné toutes ses cartes. Si le premier<br />
joueur du tour a retourné toutes ses cartes, le second joueur doit <br />
encore effectuer son action. C’est seulement à la fin de ce tour que<br />
la manche est terminée.<br />
	
### Fin du jeu<br />

Si le joueur qui a terminé en premier a un score plus grand ou égal<br />
au score de l’autre joueur et si son score est positif, une pénalité<br />
lui est attribuée : ses points sont doublés.<br />

<br />

## Membre du groupe <br />

- Nicolas Gardeur
- Rolecki Maciej
- Wattier Alexandre
- El Fahsi Abdessalam
- Viho Juste Midedji

