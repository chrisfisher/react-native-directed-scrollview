/* @flow */

export const getCellsByRow = (): Array<Row> => {
  const cellsByRow: Array<Row> = [];

  for (var rowIndex = 1; rowIndex < 10; rowIndex++) {
    let row: Row = {
      id: `row-${rowIndex}`,
      cells: []
    }

    for (var columnIndex = 1; columnIndex < 10; columnIndex++) {
      row.cells.push({
        id: `cell-${rowIndex}-${columnIndex}`,
        title: `Cell`
      });
    }

    cellsByRow.push(row)
  }

  return cellsByRow;
};

export type Cell = {
  id: string;
  title: string;
};

export type Row = {
  id: string;
  cells: Array<Cell>;
};
