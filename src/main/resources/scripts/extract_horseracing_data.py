# Program to extract data from spi_matches.csv.


import csv
import random


def main():

    horse_winodds = {}

    with open('race-result-horse.csv') as horseracing_csv:
        with open('horseracing_odds.csv', "w") as horseracing_odds_csv:

            horseracing_csv_reader = csv.reader(horseracing_csv, delimiter=',')
            horseracing_data_writer = csv.writer(horseracing_odds_csv, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            line_count = 0

            for row in horseracing_csv_reader:
                if line_count == 0:
                    print(f'Column names are {", ".join(row)}')
                    line_count += 1
                elif line_count == 30190:
                    break
                else:
                    if row[2] in horse_winodds and row[15].strip() != '---':
                        horse_winodds[row[2]].append(row[15].strip())
                    elif row[2] not in horse_winodds and row[15] != '---':
                        horse_winodds[row[2]] = [row[15].strip()]
                    line_count += 1

            horse_winloss_avg = {}

            for key, lists in horse_winodds.items():

                total = 0.0

                for value in lists:
                    total += float(value)

                # All averages in this dataset are doubles above 1. Using 1/average to make all probabilities in the range 0-1.
                average_winloss = total/len(lists)
                average_in_range = 1/average_winloss


                horse_winloss_avg[key] = average_in_range


            # print(horse_winloss_avg)

            # Create a list of the keys (horses)
            horse_names = list(horse_winloss_avg)

            print(len(set((horse_names))))

            i = 0
            total_of_prob = 0
            list_of_horses = []

            # Place every ten horses into a group
            # Calculate their probabilities of winning in this group
            # Every group of 10 horses is one horse race.
            for horse, prob in horse_winloss_avg.items():

                list_of_horses.append(horse)
                total_of_prob += prob

                if i is not 9:

                    i += 1

                else:
                    for racer in list_of_horses:
                        probability = horse_winloss_avg[racer]/total_of_prob
                        horseracing_data_writer.writerow([racer, probability])


                    i = 0
                    total_of_prob = 0
                    del list_of_horses[:]

            horseracing_csv.close()
            horseracing_odds_csv.close()


if __name__ == '__main__':


    main()

