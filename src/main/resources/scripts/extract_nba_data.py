# Program to extract data from /data/Historical-NBA-Performance.csv and
# calculate an averages of win/loss ratios over the years.


import csv


def main():

    nba_teams_and_historical_win_percentages = {}

    # Open files
    with open('Historical-NBA-Performance.csv', "r") as nba_csv_file:
        with open('NBAData.csv', "w") as nba_data:

            nba_csv_reader = csv.reader(nba_csv_file, delimiter=',')
            nba_data_writer = csv.writer(nba_data, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            line_count = 0

            for row in nba_csv_reader:
                if line_count == 0:
                    print(f'Column names are {", ".join(row)}')
                    line_count += 1
                elif line_count == 1418:
                    break
                else:
                    if row[1] in nba_teams_and_historical_win_percentages:
                        nba_teams_and_historical_win_percentages[row[1]].append(row[3].strip())
                    else:
                        nba_teams_and_historical_win_percentages[row[1]] = [row[3].strip()]
                    line_count += 1

            # print(len(nba_teams_and_historical_win_percentages.keys()))
            nba_winloss_avg = {}

            # iterate through all teams and calculate their average win/loss
            # add this to a new dictionary
            for key, list in nba_teams_and_historical_win_percentages.items():

                total = 0.0

                for value in list:
                    total += float(value)

                average_winloss = total/len(list)
                nba_winloss_avg[key] = average_winloss

            # Write win/loss average to CSV file.
            for key, value in nba_winloss_avg.items():
                nba_data_writer.writerow([key, value])

            nba_csv_file.close()
            nba_data.close()


if __name__ == '__main__':
    main()
