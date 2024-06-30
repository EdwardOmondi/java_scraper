# Test for a web scraping agent using  typescript

An agent will be developed to extract all the mayors listed for every region on the https://www.mon-maire.fr/mairesregions website.

Additional information:

1. Output File: CSV file to be delivered to the client.

> Starting page: https://www.mon-maire.fr/maires-regions
> From the starting page, the agent will visit all the regions listed on the page. They can be found under "Régions métropolitaines " and "Régions d'outre-mer ". For every mayor, the following will be extracted:

1. **Région** - region in which the mayor was found (ex Auvergne -Rhône -Alpes)

> Example of listing page: https://www.mon-maire.fr/maires-region-ile-de-france

All the mayors for every city in the selected region will be available on the listing page. For every mayor, the following will be extracted:

2. **Ville** – mayor's city found in black font (ex: Abbéville -la -Rivière (91150))
3. **Nom du maire** – mayor's name found in red after the city name (ex: M.Eric Meyer)

The agent will then visit each city/mayor's page by clicking on the mayor's name in red.

> Example of a city/mayor's page:https://www.mon-maire.fr/maire-de-abbeville-la-riviere-91

From the city/mayor's page, the following will be extracted from the "Nom du maire de..." section:

4. **Date de prise de fonction** – date of taking office. Expected format: DD/MM/YYY. The value is found after "Il a pris ses fonctions en tant que maire le.." or "Elle a pris ses fonctions en tant que maire le.." : https://www.mon-maire.fr/maire-de-barjols-83 . Here the extracted value will be "23/05/2020"

5. **URL** – mayor's / city hall's page url

> Example of a city/mayor's page: https://www.mon-maire.fr/maire-de-abbeville-la-riviere-91

From the "Contacter le maire de" section:

6. **Téléphone** - phone number found after

"Téléphone:" make sure to extract the 0 at the start (01 64 95 67 37 )

7. **Email** – found after "Email:" (mairie-abbeville-la-riviere@wanadoo.fr)

From the "Se rendre à la mairie de Ambleville " section:

8. **Adresse mairie** – the city hall's address found (Mairie de Abbéville-la-Rivière, 1 place de la Mairie, 91150 Abbéville-la-Rivière)

The columns in the output file will be in this order:

- Région
- Ville
- Nom du maire
- Date de prise de fonction
- Téléphone
- Email
- Adresse Mairie
