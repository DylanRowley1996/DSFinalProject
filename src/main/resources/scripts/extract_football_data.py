# Program to extract data from /data/spi_matches.csv.

import csv


def main():

    with open('spi_matches.csv') as spi_csv_file:
        with open('FootballData.csv', "w") as footballdata_csv:

            spi_csv_reader = csv.reader(spi_csv_file, delimiter=',')
            football_data_writer = csv.writer(footballdata_csv, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            line_count = 0

            for row in spi_csv_reader:
                if line_count == 0:
                    print(f'Column names are {", ".join(row)}')
                    line_count += 1
                else:
                    # league name, team1, team2, prob1, prob2, prob2
                    football_data_writer.writerow([row[2], row[3], row[4],row[7],row[8], row[9]])
                    line_count += 1

            spi_csv_file.close()
            footballdata_csv.close()


if __name__ == '__main__':
    main()
