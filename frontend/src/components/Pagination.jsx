export default function Pagination({ pagination }) {
    const {
        pageActuelle,
        nombrePages,
        taillePage,
        totalElements,
        changerPage,
        changerTaillePage,
    } = pagination;

    const premierElement =
        totalElements === 0 ? 0 : (pageActuelle - 1) * taillePage + 1;
    const dernierElement = Math.min(pageActuelle * taillePage, totalElements);

    return (
        <div className="pagination">
      <span className="pagination-summary">
        {premierElement}-{dernierElement} sur {totalElements}
      </span>
            <label className="pagination-size">
                Lignes par page
                <select
                    className="field"
                    value={taillePage}
                    onChange={(event) => changerTaillePage(event.target.value)}
                >
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="50">50</option>
                </select>
            </label>
            <div className="pagination-actions">
                <button
                    type="button"
                    className="button secondary small"
                    onClick={() => changerPage(pageActuelle - 1)}
                    disabled={pageActuelle === 1}
                >
                    Précédent
                </button>
                <span>
          Page {pageActuelle} / {nombrePages}
        </span>
                <button
                    type="button"
                    className="button secondary small"
                    onClick={() => changerPage(pageActuelle + 1)}
                    disabled={pageActuelle === nombrePages}
                >
                    Suivant
                </button>
            </div>
        </div>
    );
}
